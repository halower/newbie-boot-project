/*
 * Apache License
 *
 * Copyright (c) 2019  halower (halower@foxmail.com).
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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
	int joinOrder() default 0;
}
