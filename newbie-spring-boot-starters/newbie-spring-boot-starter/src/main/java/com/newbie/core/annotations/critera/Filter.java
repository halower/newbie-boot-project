package com.newbie.core.annotations.critera;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Filter {
	String propertyName() default "";
	String joinFrom () default "";
	Operator operator() default Operator.EQ;
}
