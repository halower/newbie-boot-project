package com.newbie.core.persistent.criteria;

import com.newbie.core.annotations.critera.Filter;
import com.newbie.core.annotations.critera.FilterEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class AnnotationReader {

	private Object bean;
	
	public AnnotationReader(Object bean) {
		if (!bean.getClass().isAnnotationPresent(FilterEntity.class))
			throw new IllegalArgumentException("当前bean对象： " + bean + " 没有使用@FilterEntity注解");
		this.bean = bean;
	}
	
	public Class<?> getRootEntity() {
		FilterEntity annotation = bean.getClass().getAnnotation(FilterEntity.class);
		return annotation.entityType();
	}
	
	
	public List<FilterDefinition> getFilterDefinitions() {
		List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
		for (Field field : getFilterFields()) {
			Filter filter = field.getAnnotation(Filter.class);
			FilterDefinition filterDef = new FilterDefinition();
			filterDef.setPropertyName(!filter.propertyName().isEmpty() ? filter.propertyName() : field.getName());
			filterDef.setOperator(filter.operator());
			filters.add(filterDef);
		}
		return filters;
	}
	
	protected List<Field> getFilterFields() {
		List<Field> filters = new ArrayList<Field>();
		for (Field f : bean.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(Filter.class)) filters.add(f);
		}
		return filters;
	}
	
}
