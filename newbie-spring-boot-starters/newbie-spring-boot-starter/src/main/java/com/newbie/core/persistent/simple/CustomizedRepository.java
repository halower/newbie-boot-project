package com.newbie.core.persistent.simple;
import com.newbie.core.persistent.criteria.QueryBuilder;
import com.newbie.core.utils.page.Pagination;
import lombok.var;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;


/**
 * @Author: 谢海龙
 * @Date: 2019/4/12 10:02
 * @Description
 */

@NoRepositoryBean
public interface CustomizedRepository<T,ID> extends PagingAndSortingRepository<T,ID> {
    /**
     * 新增
     * @param entity
     * @param <S>
     * @return
     */
    <S extends T> S insert(S entity);

    <S extends T> List<S> insertAll(Iterable<S> entities);
    /**
     *  修改
     * @param entity
     * @param <S>
     * @return
     */
    <S extends T> S update(S entity);
    /**
     * 新增或者修改
     * @param entity
     * @param <S>
     * @return
     */
    <S extends T> S insertOrUpdate(S entity);

    /**
     * 新增或者修改
     * @param entity
     * @param <S>
     * @return
     */
    <S extends T> S insertOrUpdate(S entity, Class<T> entityType);
    /**
     * 更新部分字段
     * @param entity 更新对象（映射字段与实体对象一致）
     * @apiNote 更新主键字段需要使用@UpdateId 注解标注
     */
     int update(Object entity, Class<T> entityType);

    <S extends T> List<S> updateAll(Iterable<S> entities);

    /**
     * 更新部分字段
     * @param entity 更新对象（映射字段与实体对象一致）
     * @apiNote 更新主键字段需要使用@UpdateId 注解标注
     */
    int updateIgnoreNull(Object entity, Class<T> entityType);

    <S extends T> List<S> updateAll(Iterable<S> entities, Class<T> entityType);

    <S extends T> List<S> updateIgnoreNull(Iterable<S> entities, Class<T> entityType);
    /**
     * 软删除
     * @param idPropName 主键对应属性名称
     * @param idValue 主键对应属性值
     * @param entityType 实体类型
     * @apiNote 更新主键字段需要使用@UpdateId 注解标注
     */
    int softDelete(String idPropName, Object idValue, Class<T> entityType);
    /**
     * 软删除
     * @param entity 普通删除Dto对象
     * @param entityType 实体类型
     */
    int softDelete(Object entity, Class<T> entityType);

    /**
     * 软删除
     * @param entity 普通删除对象
     * @param entityType 实体类型
     */
     int softDeleteWithAnyId(Object entity, Class<T> entityType);

    /**
     * 按照条件对象查询
     * @param queryFilter
     * @return 查询结果
     */
    <S> List<S> queryWithFilterE(S queryFilter);
    /**
     * 按照条件对象查询
     * @param queryFilter
     * @return
     */
    <S> Pagination<S> queryPageWithFilterE(S queryFilter, PageRequest pageRequest);

    /**
     * 按照条件对象查询
     * @param queryFilter
     * @param func 自定义条件
     * @param <S>
     * @return
     */
    <S> List<S> queryWithFilterE(S queryFilter,Function<S,String> func);


    /**
     * 按照条件对象查询
     * @param queryFilter
     * @return
     */
    <S> Pagination<S> queryPageWithFilterE(S queryFilter,Function<S,String> func, PageRequest pageRequest);
    /**
     * 按照JPQL模板查询
     * @param entity
     * @return
     */
    <S> List<S> queryWithTemplate(String tplName, String method, Object entity, Class<S> resultType);

    /**
     * 按照JPQL模板分页查询
     * @param entity
     * @return
     */
    <S> Pagination<S> queryPageWithTemplate(String tplName, String method,  Object entity,Class<S> resultType, PageRequest pageRequest);
    /**
     * 按照原生SQL模板查询
     * @param entity
     * @return
     */
     <S> List<S> nativeQueryWithTemplate( String tplName, String method, Object entity,Class<S> resultType);

    /**
     * 按照原生SQL模板分页查询
     * @param entity
     * @return
     */
     <S> Pagination<S> nativeQueryWithTemplate(String tplName, String method, Object entity,Class<S> resultType, PageRequest pageRequest);
}
