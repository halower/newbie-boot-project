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

package com.newbie.core.persistent.tpl;
import com.newbie.core.utils.Utils;
import com.newbie.core.utils.page.Pager;
import com.newbie.core.utils.page.Pagination;
import lombok.SneakyThrows;
import lombok.var;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: halower
 * @date: 2019/4/24 14:42
 *
 */
public class QueryExecutor<T> {
    @SneakyThrows
    public  List<T> query(EntityManager em, String tplName, String method, Object entity,Class<T> resultType) {
        var query = getQuery(em,tplName,method,entity,resultType,false);
        return (List<T>)query.getResultList();
    }

    @SneakyThrows
    public  List<T> nativeQuery(EntityManager em, String tplName, String method, Object entity, Class<T> resultType) {
        var query = getQuery(em,tplName,method,entity,resultType,true);
        return query.getResultList();
    }

    @SneakyThrows
    public Pagination<T>  nativeQueryPage(EntityManager em, String tplName, String method,Object entity,  Class<T> resultType,PageRequest pageRequest) {
        var query = getQuery(em,tplName,method,entity,resultType,true);
        int total = query.getResultList().size();
        int pageSize = pageRequest.getPageSize();
        int pageIndex = pageRequest.getPageNumber() + 1;
        int start = (pageIndex - 1) * pageSize;
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        List content =(List<T>)query.unwrap(NativeQuery.class).getResultList();
        return new Pager<T>().build(content,pageIndex,pageSize,total);
    }

    @SneakyThrows
    public Pagination<T> queryPage(EntityManager em, String tplName, String method, Object entity, Class<T> resultType, PageRequest pageRequest) {
        var query = getQuery(em,tplName,method,entity,resultType,false);
        int total = query.getResultList().size();
        int pageSize = pageRequest.getPageSize();
        int pageIndex = pageRequest.getPageNumber() + 1;
        int start = (pageIndex - 1) * pageSize;
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        List content =query.getResultList();
        return new Pager<T>().build(content,pageIndex,pageSize,total);
    }


    private Query getQuery(EntityManager em, String tplName, String method, Object entity,Class<T> clazz, boolean isNativeQuery) {
        var tplModel = new HashMap<String,Object>();
        var fieldMap =  Utils.bean.getFields(entity.getClass());
        var fields = fieldMap.values();
        setTemplateParameters(entity, tplModel, fields);
        var sql = new SQLTemplateResolver(tplName).getSql(method,tplModel);
        var nameParameters = getNameParameters(sql);
        Query query;
        if(isNativeQuery!=true){
             query = em.createQuery(sql,clazz);
        } else{
            query = em.createNativeQuery(sql,clazz);
        }

        setNameParameters(entity, fields, nameParameters, query);
        return query;
    }

    private void setTemplateParameters(Object entity, Map<String, Object> tplModel, Collection<Field> fields) {
        var it = fields.iterator();
        while (it.hasNext()){
            var field  = it.next();
            Object value = null;
            field.setAccessible(true);
            try {
                value = field.get(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            var key = field.getName();
            tplModel.put(key,value);
        }
    }

    private void setNameParameters(Object entity, Collection<Field> fields, ArrayList<String> nameParameters, Query query) {
        var it2 = fields.iterator();
        while (it2.hasNext()){
            var field = it2.next();
            String name = null;
            Object value = null;
            try {
                value = field.get(entity);
                name = field.getName();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(!nameParameters.contains(":"+name)) {
                continue;
            }
//            var dateFlag = field.getAnnotation(Temporal.class);
//            if (dateFlag != null) {
//                NewbieBootInfraConstants.DATE_PARAMETER_PROCESSOR.get(dateFlag).drive(query, name, (Date) value);
//            } else {
//                query.setParameter(name, value);
//            }
            query.setParameter(name, value);
        }
    }


    private ArrayList<String> getNameParameters(String sql){
        String regEx=":[\\S]+";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(sql);
        var parameters = new ArrayList<String>();
        while (matcher.find()){
            parameters.add(matcher.group());
        }
        return parameters;
    }
}
