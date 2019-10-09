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
import com.newbie.context.NewBieBootEnvUtil;
import com.newbie.core.utils.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 注意： 这里是按照项目的特殊情况进行定制的,后期可能做进一步调整
 */
@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {
    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "CJSJ", updatable = false, nullable = false)
    @TableField(value = "CJSJ", fill = FieldFill.INSERT)
    private Date cjsj;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "ZHXGSJ")
    @TableField(value = "ZHXGSJ", fill = FieldFill.INSERT_UPDATE)
    private Date zhxgsj = new Date();

    /**
     * 数据标识编号
     */
    @Column(name = "SJBSBH")
    @TableField(value = "SJBSBH", fill = FieldFill.INSERT)
    private String sjbsbh = Utils.random.getUUID();

    /**
     * 是否删除
     */
    @Column(name = "SFSC")
    @TableLogic
    @TableField(value = "SFSC", fill = FieldFill.INSERT)
    private String sfsc = "N";

    /**
     * 数据来源
     */
    @Column(name = "SJLY")
    @TableField(value = "SJLY", fill = FieldFill.INSERT)
    private String sjly;

    @PrePersist
    protected void prePersist() {
        String netID = NewBieBootEnvUtil.getContext().getEnvironment().getProperty("application.net-id");
        if(netID == null || netID != null) {
            setSjly("1");
        } else {
            this.setSjly(netID);
        }
    }
}
