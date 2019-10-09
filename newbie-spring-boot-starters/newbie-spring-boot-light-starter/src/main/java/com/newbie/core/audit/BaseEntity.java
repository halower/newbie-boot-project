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

package com.newbie.core.audit;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.newbie.core.utils.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 注意： 这里是按照项目的特殊情况进行定制的,后期可能做进一步调整
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseEntity implements Serializable {
    /**
     * 创建时间
     */
    @TableField(value = "CJSJ",fill = FieldFill.INSERT)
    private Date cjsj;

    /**
     * 最后修改时间
     */
    @TableField(value = "ZHXGSJ",fill = FieldFill.INSERT_UPDATE)
    private Date zhxgsj;

    /**
     * 数据标识编号
     */
    @TableField(value = "SJBSBH",fill = FieldFill.INSERT)
    private String sjbsbh;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "SFSC",fill = FieldFill.INSERT)
    private String sfsc;

    /**
     * 数据来源
     */
    @TableField(value = "SJLY",fill = FieldFill.INSERT)
    private String sjly;
}
