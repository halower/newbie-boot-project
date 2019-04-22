package com.newbie.core.persistent.criteria.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Filters.class)
public @interface Filter {
	String propertyName() default "";
	boolean isAnd() default true;
	Class<?> joinFrom() default Object.class;
	Operator operator() default Operator.EQ;
	Class<?> joinTo() default Object.class;
	String on() default "";
	String onTo() default "";
	Class<?> target() default Object.class;
}
