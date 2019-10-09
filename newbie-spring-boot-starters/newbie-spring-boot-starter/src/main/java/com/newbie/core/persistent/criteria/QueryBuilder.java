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

import com.newbie.constants.NewbieBootInfraConstants;
import com.newbie.core.persistent.config.FieldInfo;
import com.newbie.core.persistent.criteria.annotation.Filter;
import com.newbie.core.persistent.criteria.annotation.Filters;
import lombok.SneakyThrows;
import lombok.var;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @author: halower
 * @date: 2019/4/22 8:43
 *
 */
public class QueryBuilder<E> {
    public List<E> execute(EntityManager em, E filterEntity,Function<E,String> func) {
        var jpql = getCustomCondition(filterEntity, func);
        var query =  em.createQuery(jpql, filterEntity.getClass());
        var filedAndValues = getValidParameters(filterEntity);
        var it = filedAndValues.iterator();
        while (it.hasNext()) {
            var fi= it.next();
            var name = fi.getName();
            if( NewbieBootInfraConstants.DATE_PARAMETER_PROCESSOR.containsKey(name)) {
                NewbieBootInfraConstants.DATE_PARAMETER_PROCESSOR.get(fi.getType()).drive(query, fi.getName(), (Date) fi.getValue());
            } else {
                query.setParameter(name,fi.getValue());
            }
        }
        var result =query.getResultList();
        return (List<E>) result;
    }

    public TypedQuery<? extends Object> createQuery(EntityManager em, E filterEntity,Function<E,String> func) {
        var jpql = getCustomCondition(filterEntity, func);
        var query = em.createQuery(jpql,filterEntity.getClass());
        var filedAndValues = getValidParameters(filterEntity);
        var it = filedAndValues.iterator();
        while (it.hasNext()) {
            var field= it.next();
            query.setParameter(field.getName(),field.getValue());
        }
        return query;
    }

    public String getCustomCondition(E filterEntity,Function<E,String> func){
        var queryBuilder = new QueryMetaBuilder(filterEntity);
        var jpql = queryBuilder.buildJPQL();
        if(func==null){
            return jpql;
        }
        jpql+=func.apply(filterEntity);
        return jpql;
    }

    @SneakyThrows
    private static List<FieldInfo> getValidParameters(Object filterEntity) {
        var validParameters = new ArrayList<FieldInfo>();
        var cla = filterEntity.getClass();
        var fields = cla.getDeclaredFields();
        var fsIt = Arrays.stream(fields).iterator();
        while (fsIt.hasNext()){
            var  field = fsIt.next();
            field.setAccessible(true);
            Object value;
            var filters = field.getAnnotation(Filters.class);
            if(filters!=null) {
                for (var filter : filters.value()) {
                    value = field.get(filterEntity);
                    if(value== null || !filter.inWhere() || !filter.inOn()) {
                        continue;
                    }
                    validParameters.add(FieldInfo.builder().name(field.getName()).value(value).build());
                }
            }
            var filter = field.getAnnotation(Filter.class);
            value = field.get(filterEntity);
            if(filter!=null && value!=null && (filter.inWhere() || filter.inOn())) {
                validParameters.add(FieldInfo.builder().name(field.getName()).value(value).build());
            }
        }
        return validParameters;
    }
}

