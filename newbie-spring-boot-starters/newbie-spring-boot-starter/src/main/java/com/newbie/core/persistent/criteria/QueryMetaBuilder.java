/*
 * 版权所有 (c) 2019-2029, halower (halower@foxmail.com).
 *
 * Apache 2.0 License 同时该协议为补充协议，不允许 996 工作制度企业使用该开源软件
 *
 * 反996许可证版本1.0
 *
 * 在符合下列条件的情况下，特此免费向任何得到本授权作品的副本（包括源代码、文件和/或相关内容，以下
 * 统称为“授权作品”）的个人和法人实体授权：被授权个人或法人实体有权以任何目的处置授权作品，包括但
 * 不限于使用、复制，修改，衍生利用、散布，发布和再许可：
 *
 * 1. 个人或法人实体必须在许可作品的每个再散布或衍生副本上包含以上版权声明和本许可证，不得自行修
 * 改。
 * 2. 个人或法人实体必须严格遵守与个人实际所在地或个人出生地或归化地、或法人实体注册地或经营地
 * （以较严格者为准）的司法管辖区所有适用的与劳动和就业相关法律、法规、规则和标准。如果该司法管辖
 * 区没有此类法律、法规、规章和标准或其法律、法规、规章和标准不可执行，则个人或法人实体必须遵守国
 * 际劳工标准的核心公约。
 * 3. 个人或法人不得以任何方式诱导、暗示或强迫其全职或兼职员工或其独立承包人以口头或书面形式同意直接或
 * 间接限制、削弱或放弃其所拥有的，受相关与劳动和就业有关的法律、法规、规则和标准保护的权利或补救
 * 措施，无论该等书面或口头协议是否被该司法管辖区的法律所承认，该等个人或法人实体也不得以任何方法
 * 限制其雇员或独立承包人向版权持有人或监督许可证合规情况的有关当局报告或投诉上述违反许可证的行为
 * 的权利。
 *
 * 该授权作品是"按原样"提供，不做任何明示或暗示的保证，包括但不限于对适销性、特定用途适用性和非侵
 * 权性的保证。在任何情况下，无论是在合同诉讼、侵权诉讼或其他诉讼中，版权持有人均不承担因本软件或
 * 本软件的使用或其他交易而产生、引起或与之相关的任何索赔、损害或其他责任。
 */
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
