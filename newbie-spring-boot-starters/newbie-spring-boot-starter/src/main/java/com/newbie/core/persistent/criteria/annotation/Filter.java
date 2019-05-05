package com.newbie.core.persistent.criteria.annotation;

import com.newbie.core.persistent.criteria.enums.JoinType;
import com.newbie.core.persistent.criteria.enums.Operator;

import java.lang.annotation.*;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Filters.class)
public @interface Filter {
	String propertyName() default "";
	boolean isAnd() default true;
	boolean inSelect() default true;
	Class<?> joinFrom() default Object.class;
	JoinType joinType() default JoinType.INNER_JOIN;
	Class<?> joinTo() default Object.class;
	String on() default "";
	String onTo() default "";
	Operator onOperator() default Operator.EQ;
	Class<?> target() default Object.class;
	String targetProp() default "";
	boolean inWhere() default true;
	boolean isExtend() default false;
	String whereGroup() default "";
	boolean whereGroupIsAnd() default true;
	boolean inOn() default false;
	Operator operator() default Operator.EQ;
	int order() default 0;
}
