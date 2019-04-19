package com.newbie.core.persistent.criteria;


public class QueryBuilder {

	private AnnotationReader reader;
	
	public QueryBuilder(Object criteriaBean) {
		reader = new AnnotationReader(criteriaBean);
	}
	
	public String buildQueryString() {
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("FROM " + reader.getRootEntity().getSimpleName());
		queryBuffer.append(" alias1 ");
		queryBuffer.append("WHERE alias1." + getFirstFilter().getPropertyName());
		queryBuffer.append(" " + getFirstFilter().getOperator().getValue()  + " ");
		if (!getFirstFilter().getOperator().getValue().contains("NULL"))
			queryBuffer.append(":param1");
		return queryBuffer.toString();
	}
	private FilterDefinition getFirstFilter() {
		return reader.getFilterDefinitions().get(0);
	}
}
