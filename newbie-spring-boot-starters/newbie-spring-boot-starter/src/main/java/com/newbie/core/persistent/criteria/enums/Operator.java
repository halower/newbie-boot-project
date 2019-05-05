package com.newbie.core.persistent.criteria.enums;

public enum Operator {
		
	EQ("="), NEQ("!="), 
	GT(">"), GTE(">="), 
	LT("<"), LTE("<="),
	NULL("IS NULL"),
	BETWEEN("BETWEEN"),
	LIKE("LIKE"),
	NOT("NOT"),
	IN("IN"),
	NOT_NULL("IS NOT NULL");
	private final String value;
	
	Operator(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
