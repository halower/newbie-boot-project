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

package com.newbie.core.persistent.mybaits;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.newbie.core.exception.BusinessException;
import com.newbie.core.util.RandomUtil;
import com.newbie.dto.ResponseTypes;
import io.netty.util.internal.StringUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

import java.util.Date;

/**
 * @Author: halower
 * @Date: 2019/7/4 10:45
 */
@ConditionalOnClass({ DataSourceAutoConfiguration.class })
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    ApplicationContext applicationContext;
    
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("cjsj", new Date(), metaObject);
        this.setFieldValByName("zhxgsj", new Date(), metaObject);
        this.setFieldValByName("sfsc", "N", metaObject);
        this.setFieldValByName("sjbsbh", RandomUtil.getUUID(), metaObject);
        String networkId = applicationContext.getEnvironment().getProperty("application.network-id");
        if(StringUtil.isNullOrEmpty(networkId)) {
            throw new BusinessException(ResponseTypes.READ_FAIL, "网络标识ID未正确读取，请检查配置");
        }

        this.setFieldValByName("sjly", networkId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("zhxgsj", new Date(), metaObject);
        this.setFieldValByName("sjbsbh", RandomUtil.getUUID(), metaObject);
    }
}
