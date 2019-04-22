package com.newbie.core.persistent.criteria;
import com.newbie.core.persistent.FieldWithValue;
import com.newbie.core.persistent.criteria.annotation.Filter;
import lombok.var;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryMetaBuilder {
	private AnnotationReader reader;
	private Object filterEntity;
	public QueryMetaBuilder(Object filterEntity) {
		this.filterEntity = filterEntity;
		reader = new AnnotationReader(filterEntity);
	}
	public String buildQueryString() {
		var queryBuffer = new StringBuffer();
		var rootName = reader.getRootEntity().getSimpleName();
		var rootAlias = rootName.toLowerCase();
		queryBuffer.append("SELECT NEW ");
		queryBuffer.append(filterEntity.getClass().getName() + "( ");
		var list = getSelectedFiledWithTarget(filterEntity);
		var fit = list.iterator();
		while (fit.hasNext()){
			var rf= fit.next();
			queryBuffer.append(rf.getValue() +"."+rf.getKey());
			queryBuffer.append(fit.hasNext()? ", " : " ");
		}
		queryBuffer.append(")");
		queryBuffer.append(" FROM " + rootName);
		queryBuffer.append(" "+rootAlias);
		var joinGroups = getGroupJoinFilters();
		joinGroups.forEach((group, joins)-> {
			if(joins.size()>0) {
				queryBuffer.append(" JOIN " + group +" "+group.toLowerCase()+ " ON " );
				if(joins.size()>1) queryBuffer.append("( ");
				var jit = joins.iterator();
				while (jit.hasNext()){
					var condition = jit.next();
					var joinToType = condition.getJoinTo().getSimpleName().toLowerCase();
					var joinTo = joinToType.equals("object")? rootAlias: joinToType;
					var onRoot = StringUtils.isEmpty(condition.getOnRoot())?condition.getPropertyName():condition.getOnRoot();
					var onOther = StringUtils.isEmpty(condition.getOn())?condition.getPropertyName():condition.getOn();
					queryBuffer.append(group.toLowerCase() +"."+onOther+ "=" + joinTo +"."+onRoot);
					if(jit.hasNext()) queryBuffer.append(" AND ");
				}
				if(joins.size()>1) queryBuffer.append(" ) ");
			}
		});

		if(getAllFilters().size()>0) {
			queryBuffer.append(" WHERE ");
			var it = getWhereFilters().iterator();
			var isEnd = false;
			while (it.hasNext()){
				var filter = it.next();
				if(isEnd) {
					queryBuffer.append(filter.getIsAnd()? " AND ": " OR ");
				}
				switch (filter.getOperator()){
					case BETWEEN: {
						queryBuffer.append(rootAlias+"."+filter.getPropertyName() +" "+ filter.getOperator().getValue() +" :" + filter.getPropertyName()+"_Min" + " AND ");
						queryBuffer.append(" :" + filter.getPropertyName()+"_Max");
						break;
					}
					case NOT: {
						queryBuffer.append(rootAlias+"."+filter.getPropertyName() + " "+filter.getOperator().getValue()  +" (:"+filter.getPropertyName() +") ");
						break;
					}
					case IN: {
						queryBuffer.append(rootAlias+"."+filter.getPropertyName().replaceAll("_In","") + " "+filter.getOperator().getValue()  +" (:"+filter.getPropertyName() + ") ");
						break;
					}
					default:{
						queryBuffer.append(rootAlias+"."+filter.getPropertyName()+" "+ filter.getOperator().getValue() +" :" + filter.getPropertyName() + " ");
						break;
					}
				}
				isEnd = true;
			}
		}
		return queryBuffer.toString();
	}

	private List<FilterDefinition> getAllFilters() {
		return reader.getFilterDefinitions(false);
	}

	private List<FilterDefinition> getWhereFilters() {
		return reader.getFilterDefinitions(true);
	}

	private Map<String, List<FilterDefinition>> getGroupJoinFilters() {
		var joins = getAllFilters().stream().filter(x -> !x.getJoinFrom().getSimpleName().equals("Object"));
		return joins.collect(Collectors.groupingBy(x->x.getJoinFrom().getSimpleName()));
	}

	private static List<FieldWithValue> getSelectedFiledWithTarget(Object filterEntity) {
		var list = new ArrayList<FieldWithValue>();
		var cla = filterEntity.getClass();
		Field[] fields = cla.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			var filterFiled = field.getAnnotation(Filter.class);
			if (filterFiled ==null)  continue;
			var reader = new AnnotationReader(filterEntity);
			var defaultTarget = reader.getRootEntity().getSimpleName().toLowerCase();
			var target = filterFiled.target().getSimpleName().toLowerCase();
			if(!(field.getName().endsWith("_In") && field.getName().endsWith("_Max") && field.getName().endsWith("_Min"))) {
				list.add(FieldWithValue.builder().key(field.getName()).value(!target.equals("object")?target:defaultTarget).build());
			}
		}
		return list;
	}
}
