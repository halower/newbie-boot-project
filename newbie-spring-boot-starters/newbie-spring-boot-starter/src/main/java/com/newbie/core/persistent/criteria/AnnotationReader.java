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

import com.newbie.core.persistent.criteria.annotation.Filter;
import com.newbie.core.persistent.criteria.annotation.FilterEntity;
import com.newbie.core.persistent.criteria.annotation.Filters;
import com.newbie.core.persistent.criteria.enums.Operator;
import lombok.SneakyThrows;
import lombok.var;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnotationReader {
    private Object bean;

    public AnnotationReader(Object bean) {
        if (!bean.getClass().isAnnotationPresent(FilterEntity.class)) {
            throw new IllegalArgumentException("对象" + bean + "未使用@FilterEntity定义");
        }
        this.bean = bean;
    }

    public Class<?> getRootEntity() {
        FilterEntity annotation = bean.getClass().getAnnotation(FilterEntity.class);
        return annotation.rootEntity();
    }

    @SneakyThrows
    public List<FilterDefinition> getFilterDefinitions(boolean isWhere)  {
        var filterDefinitions = new ArrayList<FilterDefinition>();
        var fields = Arrays.stream(bean.getClass().getDeclaredFields());
        var fit = fields.iterator();
        while (fit.hasNext()){
            var field = fit.next();
            field.setAccessible(true);
            var filter = field.getAnnotation(Filter.class);
            if(filter!=null) {
                if (filtersResolver(isWhere, filterDefinitions, field, filter)) {
                    continue;
                }
            }
            var filters = field.getAnnotation(Filters.class);
            if(filters!=null) {
                var it =  Arrays.stream(filters.value()).iterator();
                while (it.hasNext()){
                    var subFilter = it.next();
                    if(subFilter!=null) {
                        if (filtersResolver(isWhere, filterDefinitions, field, subFilter)) {
                            continue;
                        }
                    }
                }
            }
        }
        return filterDefinitions;
    }

    @SneakyThrows
    private boolean filtersResolver(boolean isWhere, List<FilterDefinition> filterDefinitions, Field field, Filter filter) {
        var value = field.get(this.bean);
        if(isWhere){
            if (!filter.inWhere() || filter.isExtend()) {
                return true;
            }
            if(value==null) {
                if(filter.inWhere()){
                    if (filter.operator().equals(Operator.BETWEEN)) {
                        var max = bean.getClass().getDeclaredField(field.getName() + "_Max");
                        var min = bean.getClass().getDeclaredField(field.getName() + "_Min");
                        if (max == null || min == null) {
                            return true;
                        }
                    }
                    else if (filter.operator().equals(Operator.IN)) {
                        var in = bean.getClass().getDeclaredField(field.getName() + "_In");
                        if (in == null) {
                            return true;
                        }
                    }
                    else {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        var filterDef = new FilterDefinition();
        filterDef.setJoinFrom(filter.joinFrom());
        filterDef.setJoinTo(filter.joinTo());
        filterDef.setTarget(filter.target());
        filterDef.setJoinType(filter.joinType());
        filterDef.setIsExtend(filter.isExtend());
        filterDef.setTargetProp(filter.targetProp());
        filterDef.setJoinOrder(filter.joinOrder());
        filterDef.setOn(filter.on());
        filterDef.setInWhere(filter.inWhere());
        filterDef.setWhereGroup(StringUtils.isEmpty(filter.whereGroup())?"DEFAULT_GROUP":filter.whereGroup());
        filterDef.setWhereGroupIsAnd(filter.whereGroupIsAnd());
        filterDef.setInOn(filter.inOn());
        filterDef.setOnTo(filter.onTo());
        filterDef.setPropertyName(!filter.propertyName().isEmpty() ? filter.propertyName() : field.getName());
        filterDef.setOperator(filter.operator());
        filterDef.setIsAnd(filter.isAnd());
        filterDef.setOnOperator(filter.onOperator());
        filterDefinitions.add(filterDef);
        return false;
    }

}
