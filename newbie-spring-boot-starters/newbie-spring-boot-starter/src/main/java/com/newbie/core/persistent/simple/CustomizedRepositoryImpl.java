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

package com.newbie.core.persistent.simple;

import com.newbie.core.annotations.DeletedId;
import com.newbie.core.annotations.DeletedIds;
import com.newbie.core.annotations.UpdatedId;
import com.newbie.core.persistent.config.FieldInfo;
import com.newbie.core.persistent.criteria.QueryBuilder;
import com.newbie.core.persistent.tpl.QueryExecutor;
import com.newbie.core.utils.Utils;
import com.newbie.core.utils.page.Pager;
import com.newbie.core.utils.page.Pagination;
import lombok.SneakyThrows;
import lombok.var;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * @Author: halower
 * @Date: 2019/4/12 10:04
 *
 */
@Repository
@Transactional(readOnly = true)
public class CustomizedRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CustomizedRepository<T, ID> {
    private EntityManager em;

    public CustomizedRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.em = entityManager;
    }

    public    CustomizedRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.em = entityManager;
    }


    /**
     * 新增
     *
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    @Transactional
    public <S extends T> S insert(S entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public <S extends T> List<S> insertAll(Iterable<S> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        List<S> result = new ArrayList<S>();
        for (S entity : entities) {
            result.add(insert(entity));
        }
        return result;
    }

    /**
     * 更新
     *
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    @Transactional
    public <S extends T> S update(S entity) {
        return em.merge(entity);
    }


    @Override
    @Transactional
    public <S extends T> List<S> updateAll(Iterable<S> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        List<S> result = new ArrayList<S>();
        for (S entity : entities) {
            result.add(update(entity));
        }
        return result;
    }

    @Override
    @Transactional
    public <S extends T> List<S> updateAll(Iterable<S> entities, Class<T> entityType) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        List<S> result = new ArrayList<S>();
        for (S entity : entities) {
            update(entity, entityType);
            result.add(entity);
        }
        return result;
    }

    /**
     * 新增或者修改
     *
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    @Transactional
    public <S extends T> S insertOrUpdate(S entity) {
        if (isNew(entity)) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }


    /**
     * 新增或者修改
     *
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    @Transactional
    public <S extends T> S insertOrUpdate(S entity, Class<T> entityType) {
        if (isNew(entity)) {
            em.persist(entity);
            return entity;
        } else {
            this.update(entity, entityType);
            return entity;
        }
    }

    /**
     * 更新部分字段
     *
     * @param entity 更新对象（映射字段与实体对象一致）
     * @apiNote 更新主键字段需要使用@UpdateId 注解标注
     */
    @Override
    @Transactional
    public int update(Object entity, Class<T> entityType) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<T> criteria = cb.createCriteriaUpdate(entityType);
        Root<T> root = criteria.from(entityType);
        Map<String, Object> kvs = getFiledAndValue(entity, false);
        kvs.forEach((k, v) -> criteria.set(root.get(k), v));
        criteria.set(root.get("zhxgsj"), new Date());
        criteria.set(root.get("sjbsbh"),  Utils.random.getUUID());
        var ids = getUpdatedIds(entity);
        List<Predicate> listWhere = new ArrayList<>();
        ids.forEach(id -> {
            listWhere.add(cb.equal(root.get(id.getName()), id.getValue().toString()));
        });
        var predicatesWhereArr = new Predicate[listWhere.size()];
        var predicatesWhere = cb.and(listWhere.toArray(predicatesWhereArr));
        criteria.where(predicatesWhere);
        return em.createQuery(criteria).executeUpdate();
    }

    /**
     * 更新部分字段
     *
     * @param entity 更新对象（映射字段与实体对象一致
     * @apiNote 更新主键字段需要使用@UpdateId 注解标注
     */
    @Override
    @Transactional
    public int updateIgnoreNull(Object entity, Class<T> entityType) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<T> criteria = cb.createCriteriaUpdate(entityType);
        Root<T> root = criteria.from(entityType);
        Map<String, Object> kvs = getFiledAndValue(entity, true);
        kvs.forEach((k, v) -> criteria.set(root.get(k), v));
        criteria.set(root.get("zhxgsj"), new Date());
        criteria.set(root.get("sjbsbh"),  Utils.random.getUUID());
        var ids = getUpdatedIds(entity);
        List<Predicate> listWhere = new ArrayList<>();
        ids.forEach(id -> {
            listWhere.add(cb.equal(root.get(id.getName()), id.getValue().toString()));
        });
        var predicatesWhereArr = new Predicate[listWhere.size()];
        var predicatesWhere = cb.and(listWhere.toArray(predicatesWhereArr));
        criteria.where(predicatesWhere);
        return em.createQuery(criteria).executeUpdate();
    }

    @Override
    @Transactional
    public <S extends T> List<S> updateIgnoreNull(Iterable<S> entities, Class<T> entityType) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        List<S> result = new ArrayList<S>();
        for (S entity : entities) {
            updateIgnoreNull(entity, entityType);
            result.add(entity);
        }
        return result;
    }

    /**
     * 软删除
     *
     * @param idPropName 主键对应属性名称
     * @param idValue    主键对应属性值
     * @param entityType 实体类型
     * @apiNote 更新主键字段需要使用@UpdateId 注解标注
     */
    @Override
    @Transactional
    public int softDelete(String idPropName, Object idValue, Class<T> entityType) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaUpdate<T> criteria = builder.createCriteriaUpdate(entityType);
        Root<T> root = criteria.from(entityType);
        criteria.set(root.get("sfsc"), "Y");
        criteria.where(builder.equal(root.get(idPropName), idValue));
        return em.createQuery(criteria).executeUpdate();
    }

    /**
     * 软删除
     *
     * @param entity     普通删除对象
     * @param entityType 实体类型
     */
    @Override
    @Transactional
    public int softDelete(Object entity, Class<T> entityType) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<T> criteria = cb.createCriteriaUpdate(entityType);
        Root<T> root = criteria.from(entityType);
        criteria.set(root.get("sfsc"), "Y");
        var deletedIds = getDeletedIds(entity);
        var deletedIdss = getDeletedIdss(entity);
        List<Predicate> listWhere = new ArrayList<>();
        buildCriteriaByDeleteIdss(cb, root, deletedIdss, listWhere);
        var it = deletedIds.iterator();
        while (it.hasNext()) {
            var item = it.next();
            listWhere.add(cb.equal(root.get(item.getName()), item.getValue().toString()));
        }
        var predicatesWhereArr = new Predicate[listWhere.size()];
        var predicatesWhere = cb.and(listWhere.toArray(predicatesWhereArr));
        criteria.where(predicatesWhere);
        return em.createQuery(criteria).executeUpdate();
    }


    /**
     * 软删除
     *
     * @param entity     普通删除对象
     * @param entityType 实体类型
     */
    @Override
    @Transactional
    public int softDeleteWithAnyId(Object entity, Class<T> entityType) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<T> criteria = cb.createCriteriaUpdate(entityType);
        Root<T> root = criteria.from(entityType);
        criteria.set(root.get("sfsc"), "Y");
        var deletedIds = getDeletedIds(entity);
        var deletedIdss = getDeletedIdss(entity);
        List<Predicate> listWhere = new ArrayList<>();
        buildCriteriaByDeleteIdss(cb, root, deletedIdss, listWhere);
        var it = deletedIds.iterator();
        while (it.hasNext()) {
            var item = it.next();
            listWhere.add(cb.equal(root.get(item.getName()), item.getValue().toString()));
        }
        var predicatesWhereArr = new Predicate[listWhere.size()];
        var predicatesWhere = cb.or(listWhere.toArray(predicatesWhereArr));
        criteria.where(predicatesWhere);
        return em.createQuery(criteria).executeUpdate();
    }

    /**
     * 按照条件对象查询
     *
     * @param queryFilter
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public <S> List<S> queryWithFilterE(S queryFilter, Function<S, String> func) {
        var data = new QueryBuilder<S>().execute(em, queryFilter, func);
        return data;
    }

    /**
     * 按照条件对象查询
     *
     * @param queryFilter
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public <S> List<S> queryWithFilterE(S queryFilter) {
        var data = new QueryBuilder<S>().execute(em, queryFilter, null);
        return data;
    }

    /**
     * 按照条件对象查询
     *
     * @param queryFilter
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public <S> Pagination<S> queryPageWithFilterE(S queryFilter, PageRequest pageRequest) {
        return queryPageWithFilterE(queryFilter, null, pageRequest);
    }

    /**
     * 按照条件对象查询
     *
     * @param queryFilter
     * @return
     */
    @Override
    public <S> Pagination<S> queryPageWithFilterE(S queryFilter, Function<S, String> func, PageRequest pageRequest) {
        var query = new QueryBuilder<S>().createQuery(em, queryFilter, func);
        int total = query.getResultList().size();
        int pageSize = pageRequest.getPageSize();
        int pageIndex = pageRequest.getPageNumber() + 1;
        int start = (pageIndex - 1) * pageSize;
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        var content = (List<S>) query.getResultList();
        var pagination = new Pager<S>().build(content, pageIndex, pageSize, total);
        return pagination;
    }


    /***
     * 按照JPQL模板查询
     * @param tplName 模板名称
     * @param method  对应方法
     * @param model   参数对象
     * @param resultType 返回类型
     */
    @Override
    public <S> List<S> queryWithTemplate(String tplName, String method, Object model, Class<S> resultType) {
        return new QueryExecutor<S>().query(em, tplName, method, model, resultType);
    }

    /***
     * 按照JPQL模板查询
     * @param tplName 模板名称
     * @param method  对应方法
     * @param model   参数对象
     * @param resultType 返回类型
     */
    @Override
    public <S> Pagination<S> queryPageWithTemplate(String tplName, String method, Object model, Class<S> resultType, PageRequest pageRequest) {
        return new QueryExecutor<S>().queryPage(em, tplName, method, model, resultType, pageRequest);
    }


    /**
     * 按照原生SQL模板模板查询
     *
     * @param entity
     * @return
     */
    @Override
    public <S> List<S> nativeQueryWithTemplate(String tplName, String method, Object entity, Class<S> resultType) {
        return new QueryExecutor<S>().nativeQuery(em, tplName, method, entity, resultType);
    }

    /**
     * 按照SQL模板分页查询
     *
     * @param entity
     * @return
     */
    @Override
    public <S> Pagination<S> nativeQueryWithTemplate(String tplName, String method, Object entity, Class<S> resultType, PageRequest pageRequest) {
        return new QueryExecutor<S>().nativeQueryPage(em, tplName, method, entity, resultType, pageRequest);
    }

    private void buildCriteriaByDeleteIdss(CriteriaBuilder cb, Root<T> root, List<FieldInfo> deletedIdss, List<Predicate> listWhere) {
        deletedIdss.forEach(id -> {
            if ((id.getValue() == null) || (((Collection) id.getValue()).size() == 0)) {
                return;
            }
            Iterator iterator = ((Collection) id.getValue()).iterator();
            CriteriaBuilder.In in = cb.in(root.get(id.getName()));
            while (iterator.hasNext()) {
                in.value(iterator.next());
            }
            listWhere.add(in);
        });
    }

    private static Map<String, Object> getFiledAndValue(Object obj, boolean ignoreNullFields) {
        var map = new HashMap<String, Object>();
        Field[] fields = Utils.bean.getFields(obj);
        for (Field field : fields) {
            if(field.getName() == "zhxgsj" || field.getName() == "sjbsbh") {
                continue;
            }
            field.setAccessible(true);
            Object val = null;
            try {
                try {
                    val = field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (ignoreNullFields) {
                    UpdatedId idFlag = field.getDeclaredAnnotation(UpdatedId.class);
                    if (val != null && idFlag == null) {
                        map.put(field.getName(), val);
                    }
                } else {
                    map.put(field.getName(), val);
                }

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private List<FieldInfo> getUpdatedIds(Object entity) {
        var kvs = new ArrayList<FieldInfo>();
        var cla = entity.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            UpdatedId id = field.getDeclaredAnnotation(UpdatedId.class);
            if (id != null) {
                var kv = fetchId(entity, field);
                kvs.add(kv);
            }
        }
        return kvs;
    }

    private List<FieldInfo> getDeletedIds(Object entity) {
        var kvs = new ArrayList<FieldInfo>();
        var cla = entity.getClass();
        var fields = cla.getDeclaredFields();
        var it = Arrays.stream(fields).iterator();
        while (it.hasNext()) {
            var field = it.next();
            field.setAccessible(true);
            var id = field.getDeclaredAnnotation(DeletedId.class);
            if (id != null) {
                var kv = fetchId(entity, field);
                kvs.add(kv);
            }
        }
        return kvs;
    }

    private List<FieldInfo> getDeletedIdss(Object entity) {
        var kvs = new ArrayList<FieldInfo>();
        var cla = entity.getClass();
        Field[] fields = cla.getDeclaredFields();
        var it = Arrays.stream(fields).iterator();
        while (it.hasNext()) {
            var field = it.next();
            field.setAccessible(true);
            var id = field.getDeclaredAnnotation(DeletedIds.class);
            if (id != null) {
                var kv = fetchId(entity, field);
                kvs.add(kv);
            }
        }
        return kvs;
    }

    @SneakyThrows
    private List<FieldInfo> getId(Object entity) {
        var kvs = new ArrayList<FieldInfo>();
        var cla = entity.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            var idAnnotation = field.getDeclaredAnnotation(Id.class);
            if (idAnnotation != null && field.get(entity) != null) {
                var kv = fetchId(entity, field);
                kvs.add(kv);
            }
        }
        return kvs;
    }

    private boolean isNew(Object entity) {
        return getId(entity).size() == 0;
    }

    private FieldInfo fetchId(Object dto, Field field) {
        var name = field.getName();
        Object value = null;
        try {
            value = field.get(dto);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return FieldInfo.builder().name(name).value(value).build();
    }
}