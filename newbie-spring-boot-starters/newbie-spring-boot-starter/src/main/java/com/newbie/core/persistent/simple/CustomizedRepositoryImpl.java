package com.newbie.core.persistent.simple;

import com.newbie.core.annotations.DeletedId;
import com.newbie.core.annotations.UpdatedId;
import lombok.var;
import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 谢海龙
 * @Date: 2019/4/12 10:04
 * @Description
 */
@Repository
@Transactional(readOnly = true)
public class CustomizedRepositoryImpl<T,ID extends Serializable> extends SimpleJpaRepository<T,ID>  implements CustomizedRepository<T,ID>
{

    private  EntityManager em;

    public CustomizedRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.em = entityManager;
    }

    public CustomizedRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.em = entityManager;
    }


    /**
     * 新增
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

    /**
     * 更新
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    @Transactional
    public <S extends T> S update(S entity) {
        return em.merge(entity);
    }

    /**
     * 新增或者修改
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
     * 更新部分字段
     * @param entity 更新对象（映射字段与实体对象一致）
     * @apiNote 更新主键字段需要使用@UpdateId 注解标注
     */
    @Override
    public void update(Object entity,Class<T> entityType) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<T> criteria = cb.createCriteriaUpdate(entityType);
        Root<T> root = criteria.from(entityType);
        Map<String, Object> kvs = getFiledAndValue(entity,false);
        kvs.forEach((k, v) -> criteria.set(root.get(k), v));
        var ids = getUpdatedIds(entity);
        List<Predicate> listWhere=new ArrayList<>();
        ids.forEach(id -> {
            listWhere.add(cb.equal(root.get(id.getKey()), id.getValue().toString()));
        });
        var predicatesWhereArr=new Predicate[listWhere.size()];
        var predicatesWhere= cb.and(listWhere.toArray(predicatesWhereArr));
        criteria.where(predicatesWhere);
        em.createQuery(criteria).executeUpdate();
    }

    /**
     * 更新部分字段
     * @param entity 更新对象（映射字段与实体对象一致
     * @apiNote 更新主键字段需要使用@UpdateId 注解标注
     */
    @Override
    public void updateIgnoreNull(Object entity,Class<T> entityType) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<T> criteria = cb.createCriteriaUpdate(entityType);
        Root<T> root = criteria.from(entityType);
        Map<String, Object> kvs = getFiledAndValue(entity,true);
        kvs.forEach((k, v) -> criteria.set(root.get(k), v));
        var ids = getUpdatedIds(entity);
        List<Predicate> listWhere=new ArrayList<>();
        ids.forEach(id -> {
            listWhere.add(cb.equal(root.get(id.getKey()), id.getValue().toString()));
        });
        var predicatesWhereArr=new Predicate[listWhere.size()];
        var predicatesWhere= cb.and(listWhere.toArray(predicatesWhereArr));
        criteria.where(predicatesWhere);
        em.createQuery(criteria).executeUpdate();
    }

    /**
     * 软删除
     * @param idPropName 主键对应属性名称
     * @param idValue 主键对应属性值
     * @param entityType 实体类型
     * @apiNote 更新主键字段需要使用@UpdateId 注解标注
     */
    @Override
    public void softDelete(String idPropName, Object idValue, Class<T> entityType){
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaUpdate<T> criteria = builder.createCriteriaUpdate(entityType);
        Root<T> root = criteria.from(entityType);
        criteria.set(root.get("sfsc"),"Y");
        criteria.where(builder.equal(root.get(idPropName), idValue));
        em.createQuery(criteria).executeUpdate();
    }

    /**
     * 软删除
     * @param entity 普通删除对象
     * @param entityType 实体类型
     */
    @Override
    public void softDelete(Object entity, Class<T> entityType){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<T> criteria = cb.createCriteriaUpdate(entityType);
        Root<T> root = criteria.from(entityType);
        criteria.set(root.get("sfsc"),"Y");
        var deletedIds = getDeletedIds(entity);
        List<Predicate> listWhere=new ArrayList<>();
        deletedIds.forEach(id -> {
            listWhere.add(cb.equal(root.get(id.getKey()), id.getValue().toString()));
        });
        var predicatesWhereArr=new Predicate[listWhere.size()];
        var predicatesWhere= cb.and(listWhere.toArray(predicatesWhereArr));
        criteria.where(predicatesWhere);
        em.createQuery(criteria).executeUpdate();
    }
    /**
     * 软删除
     * @param entity 普通删除对象
     * @param entityType 实体类型
     */
    @Override
    public void softDeleteWithAnyId(Object entity, Class<T> entityType){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<T> criteria = cb.createCriteriaUpdate(entityType);
        Root<T> root = criteria.from(entityType);
        criteria.set(root.get("sfsc"),"Y");
        var deletedIds = getDeletedIds(entity);
        List<Predicate> listWhere=new ArrayList<>();
        deletedIds.forEach(id -> {
            listWhere.add(cb.equal(root.get(id.getKey()), id.getValue().toString()));
        });
        var predicatesWhereArr=new Predicate[listWhere.size()];
        var predicatesWhere= cb.or(listWhere.toArray(predicatesWhereArr));
        criteria.where(predicatesWhere);
        em.createQuery(criteria).executeUpdate();
    }


    private static Map<String, Object> getFiledAndValue(Object obj,boolean ignoreNullFields) {
        var map = new HashMap<String, Object>();
        Class cla = obj.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object val = null;
            try {
                try {
                    val = field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if(ignoreNullFields) {
                    UpdatedId idFlag = field.getDeclaredAnnotation(UpdatedId.class);
                    if (val != null && idFlag==null) map.put(field.getName(), val);
                } else map.put(field.getName(), val);

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private List<KV> getUpdatedIds(Object entity){
        var kvs = new ArrayList<KV>();
        var cla = entity.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            UpdatedId id = field.getDeclaredAnnotation(UpdatedId.class);
            if(id!=null) {
                var kv =  fetchId(entity,field);
                kvs.add(kv);
            }
        }
        return kvs;
    }

    private List<KV>  getDeletedIds(Object entity){
        var kvs = new ArrayList<KV>();
        var cla = entity.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            DeletedId id = field.getDeclaredAnnotation(DeletedId.class);
            if(id!=null) {
                var kv =  fetchId(entity,field);
                kvs.add(kv);
            }
        }
        return kvs;
    }

    private  List<KV> getId(Object entity){
        var kvs = new ArrayList<KV>();
        var cla = entity.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            var id = field.getDeclaredAnnotation(Id.class);
            if(id!=null) {
                var kv =  fetchId(entity,field);
                kvs.add(kv);
            }
        }
        return kvs;
    }

    private  boolean isNew(Object entity){
        return getId(entity).size()==0;
    }

    private KV fetchId(Object dto, Field field) {
        var name = field.getName();
        Object value = null;
        try {
            value = field.get(dto);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return KV.builder().key(name).value(value).build();
    }

}