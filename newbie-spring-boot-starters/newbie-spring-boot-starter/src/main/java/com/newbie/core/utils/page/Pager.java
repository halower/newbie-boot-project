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

package com.newbie.core.utils.page;

import lombok.var;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: halower
 * @date: 2019/4/13 15:00
 *
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
