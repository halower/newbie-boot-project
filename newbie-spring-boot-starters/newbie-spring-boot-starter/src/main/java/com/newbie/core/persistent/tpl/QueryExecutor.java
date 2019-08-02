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
