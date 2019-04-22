package com.newbie.core.persistent.criteria;

import com.newbie.core.persistent.FieldWithValue;
import lombok.var;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 谢海龙
 * @Date: 2019/4/22 8:43
 * @Description
 */
public class QueryBuilder<E> {
    public List<E> execute(EntityManager em, QueryFilter filterEntity) {
        var queryBuilder = new QueryMetaBuilder(filterEntity);
        var jpql = queryBuilder.buildQueryString();
        var query = em.createQuery(jpql,filterEntity.getClass());
        var filedAndValues = getFieldWithValues(filterEntity);
        var it = filedAndValues.iterator();
        while (it.hasNext()) {
            var f= it.next();
            query.setParameter(f.getKey(),f.getValue());
        }
        var result =query.getResultList();
        return (List<E>) result;
    }

    public TypedQuery<?> createQuery(EntityManager em, QueryFilter filterEntity) {
        var queryBuilder = new QueryMetaBuilder(filterEntity);
        var jpql = queryBuilder.buildQueryString();
        var query = em.createQuery(jpql,filterEntity.getClass());
        var filedAndValues = getFieldWithValues(filterEntity);
        var it = filedAndValues.iterator();
        while (it.hasNext()) {
            var f= it.next();
            query.setParameter(f.getKey(),f.getValue());
        }
        return query;
    }

    private static List<FieldWithValue> getFieldWithValues(Object filterEntity) {
        var list = new ArrayList<FieldWithValue>();
        var cla = filterEntity.getClass();
        var fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(filterEntity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(value!=null) list.add(FieldWithValue.builder().key(field.getName()).value(value).build());
        }
        return list;
    }
}
