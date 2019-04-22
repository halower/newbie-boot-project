package com.newbie.core.persistent.criteria;

import com.newbie.core.persistent.criteria.annotation.Operator;
import lombok.Data;

@Data
public class FilterDefinition {
	private String propertyName;
	private Operator operator;
	private Boolean isAnd;
	private Class<?> joinFrom;
	private Class<?> joinTo;
	private Object value;
	private String on;
	private String onRoot;
	private Class<?> target;
}
