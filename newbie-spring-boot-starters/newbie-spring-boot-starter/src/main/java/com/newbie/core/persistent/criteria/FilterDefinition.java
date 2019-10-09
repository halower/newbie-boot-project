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

package com.newbie.core.persistent.criteria;

import com.newbie.core.persistent.criteria.enums.JoinType;
import com.newbie.core.persistent.criteria.enums.Operator;
import lombok.Data;

/**
 * @author: halower
 * @date: 2019/4/24 11:43
 *
 */
@Data
public class FilterDefinition {
    private String propertyName;
    private Operator operator;
    private Operator onOperator;
    private Boolean isAnd;
    private Boolean inWhere;
    private String whereGroup;
    private Boolean whereGroupIsAnd;
    private Boolean inSelect;
    private Boolean inOn;
    private Class<?> joinFrom;
    private Class<?> joinTo;
    private String on;
    private String onTo;
    private String targetProp;
    private JoinType joinType;
    private Object value;
    private Class<?> target;
    private Boolean isExtend;
    private Integer joinOrder;
}