package com.newbie.core.persistent.simple;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;


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
     * 更新部分字段
     * @param entity 更新对象（映射字段与实体对象一致）
     * @apiNote 更新主键字段需要使用@UpdateId 注解标注
     */
     void update(Object entity,Class<T> entityType);

    /**
     * 更新部分字段
     * @param entity 更新对象（映射字段与实体对象一致）
     * @apiNote 更新主键字段需要使用@UpdateId 注解标注
     */
    void updateIgnoreNull(Object entity,Class<T> entityType);
    /**
     * 软删除
     * @param idPropName 主键对应属性名称
     * @param idValue 主键对应属性值
     * @param entityType 实体类型
     * @apiNote 更新主键字段需要使用@UpdateId 注解标注
     */
    void softDelete(String idPropName, Object idValue, Class<T> entityType);
    /**
     * 软删除
     * @param entity 普通删除Dto对象
     * @param entityType 实体类型
     */
    void softDelete(Object entity, Class<T> entityType);

    /**
     * 软删除
     * @param entity 普通删除对象
     * @param entityType 实体类型
     */
     void softDeleteWithAnyId(Object entity, Class<T> entityType);
}
