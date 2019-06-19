package com.newbie.core.utils.page;

import lombok.var;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: 谢海龙
 * @Date: 2019/4/13 15:00
 * @Description
 */    //new Pager<UserDeptRole>()
public class Pager<T> {
    /**
     * 分页
     * @param page JPA原生分页对象
     * @return
     */
    public Pagination build(Page<T> page){
        var data = page.getContent();
        return new Pagination(data,page.getNumber() + 1 , page.getSize() ,page.getTotalPages());
    }

    /**
     * 分页
     * @param content 每页要显示元素
     * @param pageIndex 当前页码
     * @param pageSize 每页大小
     * @return
     */
    public Pagination build(List<T> content, int pageIndex,int pageSize, long total){
        return new Pagination<>(content, pageIndex, pageSize, total);
    }
}
