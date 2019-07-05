/*
 * 版权所有 (c) 2019-2029, halower (halower@foxmail.com).
 *
 * Apache 2.0 License 同时该协议为补充协议，不允许 996 工作制度企业使用该开源软件
 *
 * 反996许可证版本1.0
 *
 * 在符合下列条件的情况下，特此免费向任何得到本授权作品的副本（包括源代码、文件和/或相关内容，以下
 * 统称为“授权作品”）的个人和法人实体授权：被授权个人或法人实体有权以任何目的处置授权作品，包括但
 * 不限于使用、复制，修改，衍生利用、散布，发布和再许可：
 *
 * 1. 个人或法人实体必须在许可作品的每个再散布或衍生副本上包含以上版权声明和本许可证，不得自行修
 * 改。
 * 2. 个人或法人实体必须严格遵守与个人实际所在地或个人出生地或归化地、或法人实体注册地或经营地
 * （以较严格者为准）的司法管辖区所有适用的与劳动和就业相关法律、法规、规则和标准。如果该司法管辖
 * 区没有此类法律、法规、规章和标准或其法律、法规、规章和标准不可执行，则个人或法人实体必须遵守国
 * 际劳工标准的核心公约。
 * 3. 个人或法人不得以任何方式诱导、暗示或强迫其全职或兼职员工或其独立承包人以口头或书面形式同意直接或
 * 间接限制、削弱或放弃其所拥有的，受相关与劳动和就业有关的法律、法规、规则和标准保护的权利或补救
 * 措施，无论该等书面或口头协议是否被该司法管辖区的法律所承认，该等个人或法人实体也不得以任何方法
 * 限制其雇员或独立承包人向版权持有人或监督许可证合规情况的有关当局报告或投诉上述违反许可证的行为
 * 的权利。
 *
 * 该授权作品是"按原样"提供，不做任何明示或暗示的保证，包括但不限于对适销性、特定用途适用性和非侵
 * 权性的保证。在任何情况下，无论是在合同诉讼、侵权诉讼或其他诉讼中，版权持有人均不承担因本软件或
 * 本软件的使用或其他交易而产生、引起或与之相关的任何索赔、损害或其他责任。
 */
package com.newbie.core.persistent.simple;
import com.newbie.core.utils.page.Pagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.function.Function;


/**
 * @author: 谢海龙
 * @date: 2019/4/12 10:02
 *
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
