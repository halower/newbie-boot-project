package com.newbie.core.persistent.criteria;


import com.newbie.core.annotations.critera.Operator;
import lombok.Data;

@Data
public class FilterDefinition {
	private String propertyName;
	private String joinFrom;
	private Operator operator;
	private Object value;
}
