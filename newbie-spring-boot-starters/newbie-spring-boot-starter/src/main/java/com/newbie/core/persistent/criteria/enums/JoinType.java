package com.newbie.core.persistent.criteria.enums;

public enum JoinType {
	INNER_JOIN("INNER JOIN"),
	LEFT_JOIN("LEFT JOIN"),
	RIGHT_JOIN("RIGHT JOIN"),
	CROSS_JOIN("CROSS JOIN");
	private final String value;

	JoinType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
