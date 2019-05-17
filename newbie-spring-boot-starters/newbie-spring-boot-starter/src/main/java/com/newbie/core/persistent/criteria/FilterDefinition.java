package com.newbie.core.persistent.criteria;

import com.newbie.core.persistent.criteria.enums.JoinType;
import com.newbie.core.persistent.criteria.enums.Operator;
import lombok.Data;

/**
 * @Author: 谢海龙
 * @Date: 2019/4/24 11:43
 * @Description
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