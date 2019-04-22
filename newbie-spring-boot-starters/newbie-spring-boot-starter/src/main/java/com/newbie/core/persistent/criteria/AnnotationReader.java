package com.newbie.core.persistent.criteria;

import com.newbie.core.persistent.criteria.annotation.Filter;
import com.newbie.core.persistent.criteria.annotation.FilterEntity;
import com.newbie.core.persistent.criteria.annotation.Filters;
import lombok.var;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationReader {
	private Object bean;
	
	public AnnotationReader(Object bean) {
		if (!bean.getClass().isAnnotationPresent(FilterEntity.class))
			throw new IllegalArgumentException("对象" + bean + "未使用@FilterEntity定义");
		this.bean = bean;
	}
	
	public Class<?> getRootEntity() {
		FilterEntity annotation = bean.getClass().getAnnotation(FilterEntity.class);
		return annotation.rootEntity();
	}
	
	
	public List<FilterDefinition> getFilterDefinitions(boolean isWhere)  {
		List<FilterDefinition> filterDefinitions = new ArrayList<FilterDefinition>();
		for (Field field: getFiltersFields()){
			var filters = field.getAnnotation(Filters.class);
			if(filters!=null) {
				for (var filter: filters.value()) {
					field.setAccessible(true);
					Object value = null;
					try {
						value = field.get(this.bean);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					List<FilterDefinition> x = getFilterDefinitions(isWhere, field, filter, value);
					if (x != null) return x;
					var filterDef = new FilterDefinition();
					filterDef.setJoinFrom(filter.joinFrom());
					filterDef.setJoinTo(filter.joinTo());
					filterDef.setTarget(filter.target());
					filterDef.setOn(filter.on());
					filterDef.setOnRoot(filter.onTo());
					filterDef.setPropertyName(!filter.propertyName().isEmpty() ? filter.propertyName() : field.getName());
					filterDef.setOperator(filter.operator());
					filterDef.setIsAnd(filter.isAnd());
					filterDefinitions.add(filterDef);
				}
		    }
		}
		for (Field field: getFilterFields()) {
			var filter = field.getAnnotation(Filter.class);
			field.setAccessible(true);
			Object value = null;
			try {
				value = field.get(this.bean);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			getFilterDefinitions(isWhere, field, filter, value);
			var filterDef = new FilterDefinition();
			filterDef.setJoinFrom(filter.joinFrom());
			filterDef.setJoinTo(filter.joinTo());
			filterDef.setTarget(filter.target());
			filterDef.setOn(filter.on());
			filterDef.setPropertyName(!filter.propertyName().isEmpty() ? filter.propertyName() : field.getName());
			filterDef.setOperator(filter.operator());
			filterDef.setIsAnd(filter.isAnd());
			filterDefinitions.add(filterDef);
		}
		return filterDefinitions;
	}

	private List<FilterDefinition> getFilterDefinitions(boolean isWhere, Field field, Filter filter, Object value) {
		if(isWhere){
		  if(value==null) {
			  switch (filter.operator()){
				  case BETWEEN: {
					  try {
						  var max = bean.getClass().getDeclaredField(field.getName() + "_Max");
						  var min = bean.getClass().getDeclaredField(field.getName() + "_Min");
						  if(max==null || min ==null) break;
					  } catch (NoSuchFieldException e) {
						  e.printStackTrace();
					  }
					  break;
				  }
				  case IN: {
					  try {
						  var in = bean.getClass().getDeclaredField(field.getName() + "_In");
						  if(in==null) break;
					  } catch (NoSuchFieldException e) {
						  e.printStackTrace();
					  }
					  break;
				  }
			  }
		  }
		}
		return null;
	}


	protected List<Field> getFilterFields() {
		List<Field> filters = new ArrayList<Field>();
		for (Field f : bean.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(Filter.class)) filters.add(f);
		}
		return filters;
	}

	protected List<Field> getFiltersFields() {
	  List<Field> filters = new ArrayList<Field>();
		for (Field f : bean.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(Filters.class)) filters.add(f);
		}
		return filters;
	}
}
