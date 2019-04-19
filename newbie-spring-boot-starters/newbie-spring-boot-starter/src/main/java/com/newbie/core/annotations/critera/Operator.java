package com.newbie.core.annotations.critera;

public enum Operator {

	EQ("="), NEQ("!="), 
	GT(">"), GTE(">="), 
	LT("<"), LTE("<="), 
	NULL("IS NULL"), 
	NOT_NULL("IS NOT NULL");

	private final String value;
	
	Operator(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
