package com.newbie.core.persistent.criteria;
import com.newbie.core.persistent.config.FieldInfo;
import com.newbie.core.persistent.criteria.annotation.Filter;
import com.newbie.core.persistent.criteria.annotation.Filters;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import lombok.var;
import java.util.stream.Collectors;

public class QueryMetaBuilder {
	private AnnotationReader reader;
	private Object filterEntity;
	public QueryMetaBuilder(Object filterEntity) {
		this.filterEntity = filterEntity;
		reader = new AnnotationReader(filterEntity);
	}
	public String buildJPQL() {
		var queryBuffer = new StringBuffer();
		var rootName = reader.getRootEntity().getSimpleName();
		var rootAlias = rootName.toLowerCase();
		queryBuffer.append("SELECT NEW ");
		queryBuffer.append(filterEntity.getClass().getName() + "( ");
		var list = getSelectedFiledWithTarget(filterEntity);
		var fit = list.iterator();
		while (fit.hasNext()){
			var rf= fit.next();
			queryBuffer.append(rf.getValue() +"."+rf.getName());
			queryBuffer.append(fit.hasNext()? ", " : " ");
		}
		queryBuffer.append(")");
		queryBuffer.append(" FROM " + rootName);
		queryBuffer.append(" "+rootAlias);
		var joinGroups = getGroupJoinFilters();
		joinGroups.forEach((group, joins)-> {
			if(joins.size()>0) {
				var joinType = joins.get(0).getJoinType().getValue();
				queryBuffer.append(" "+ joinType + " " + group +" "+group.toLowerCase()+ " ON " );
				if(joins.size()>1) {
					queryBuffer.append("( ");
				}
				var jit = joins.iterator();
				while (jit.hasNext()){
					var filter = jit.next();
					var joinToType = filter.getJoinTo().getSimpleName().toLowerCase();
					var joinTo = joinToType.equals("object")? rootAlias: joinToType;
					var onTo = StringUtils.isEmpty(filter.getOnTo())?filter.getPropertyName():filter.getOnTo();
					var onOther = StringUtils.isEmpty(filter.getOn())?filter.getPropertyName():filter.getOn();
					if(filter.getInOn()){
						queryBuffer.append(group.toLowerCase() +"."+onOther + filter.getOnOperator().getValue()+":"+onOther);
					} else {
						queryBuffer.append(group.toLowerCase() +"."+onOther+ "=" + joinTo +"."+onTo);
					}
					if(jit.hasNext()) {
						queryBuffer.append(" AND ");
					}
				}
				if(joins.size()>1) {
					queryBuffer.append(" ) ");
				}
			}
		});
		var whereFilters =  getWhereFilters();
		if(whereFilters.size()>0) {
			queryBuffer.append(" WHERE ");
			if(whereFilters.size()> 1) {
				var group = whereFilters.entrySet().iterator();
				var isGroupStart = true;
				while (group.hasNext()) {
					var currGroup = group.next().getValue();
					if (!isGroupStart) {
						var groupWherePredicate = currGroup.stream().filter(x->x.getWhereGroupIsAnd()).collect(Collectors.toList());
						queryBuffer.append(groupWherePredicate.size()>0 ? " AND " : " OR ");
					}
					queryBuffer.append(" ( ");
					var currGroupItems = currGroup.stream().iterator();
					var isGroupInnerStart = true;
					buildWherePredicate(queryBuffer, rootAlias, isGroupInnerStart, currGroupItems);
					queryBuffer.append(" ) ");
					isGroupStart = false;
				}
			}
			else {
				var it  = whereFilters.get("DEFAULT_GROUP").iterator();
				var isEnd = true;
				buildWherePredicate(queryBuffer, rootAlias, isEnd, it);
			}
		}
		return queryBuffer.toString();
	}

	private void buildWherePredicate(StringBuffer queryBuffer, String rootAlias, boolean isGroupInnerStart, Iterator<FilterDefinition> currGroupItems) {
		while (currGroupItems.hasNext()) {
			var filter = currGroupItems.next();
			if (!isGroupInnerStart) {
				queryBuffer.append(filter.getIsAnd() ? " AND " : " OR ");
			}
			var whereTarget = filter.getTarget().getSimpleName().equals("Object") ? rootAlias : filter.getTarget().getSimpleName().toLowerCase();
			var whereTargetProp = StringUtils.isEmpty(filter.getTargetProp()) ? filter.getPropertyName() : filter.getTargetProp();
			switch (filter.getOperator()){
				case BETWEEN: {
					queryBuffer.append(whereTarget+"."+ whereTargetProp +" "+ filter.getOperator().getValue() +" :" + filter.getPropertyName()+"_Min" + " AND :" + filter.getPropertyName()+"_Max");
					break;
				}
				case NOT: {
					queryBuffer.append(whereTarget+"."+ whereTargetProp + " "+filter.getOperator().getValue()  +" (:"+filter.getPropertyName() +") ");
					break;
				}
				case IN: {
					queryBuffer.append(whereTarget+"."+ whereTargetProp.replaceAll("_In","") + " "+filter.getOperator().getValue()  +" (:"+filter.getPropertyName() + ") ");
					break;
				}
				default:{
					queryBuffer.append(whereTarget+"."+ whereTargetProp +" "+ filter.getOperator().getValue() +" :" + filter.getPropertyName() + " ");
					break;
				}
			}
			isGroupInnerStart = false;
		}
	}

	private List<FilterDefinition> getAllFilters() {
		return reader.getFilterDefinitions(false);
	}

	private LinkedHashMap<String, List<FilterDefinition>> getWhereFilters() {
		var wherePredicates = reader.getFilterDefinitions(true);
		var whereGroup =wherePredicates.stream().collect(Collectors.groupingBy(o -> o.getWhereGroup(), LinkedHashMap::new,Collectors.toList()));
		return whereGroup;
	}

	private Map<String, List<FilterDefinition>> getGroupJoinFilters() {
		var joins = getAllFilters().stream()
				.sorted(Comparator.comparingInt(FilterDefinition::getJoinOrder))
				.filter(x -> !x.getJoinFrom().getSimpleName().equals("Object"));
		return joins.collect(Collectors.groupingBy(o -> o.getJoinFrom().getSimpleName(), LinkedHashMap::new,Collectors.toList()));
	}

	private static List<FieldInfo> getSelectedFiledWithTarget(Object filterEntity) {
		var list = new ArrayList<FieldInfo>();
		var cla = filterEntity.getClass();
		var fields = cla.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			var filters = field.getAnnotation(Filters.class);
            var reader = new AnnotationReader(filterEntity);
            var defaultTarget = reader.getRootEntity().getSimpleName().toLowerCase();
			if(filters!=null) {
				for (var filter : filters.value()) {
					if(!filter.inSelect()) {
						continue;
					}
					var target = filter.target().getSimpleName().toLowerCase();
					var targetProp = StringUtils.isNotEmpty(filter.targetProp())?  filter.targetProp(): field.getName();
					if(!(field.getName().endsWith("_In") && field.getName().endsWith("_Max") && field.getName().endsWith("_Min"))) {
						list.add(FieldInfo.builder().name(targetProp).value(!target.equals("object")?target:defaultTarget).build());
					}
				}
			}
			var filter = field.getAnnotation(Filter.class);
			if(filter!=null && filter.inSelect()) {
				var target = filter.target().getSimpleName().toLowerCase();
				var targetProp = StringUtils.isNotEmpty(filter.targetProp())?  filter.targetProp(): field.getName();
				if(!(field.getName().endsWith("_In") && field.getName().endsWith("_Max") && field.getName().endsWith("_Min"))) {
					list.add(FieldInfo.builder().name(targetProp).value(!target.equals("object")?target:defaultTarget).build());
				}
			}
		}
		return list;
	}
}
