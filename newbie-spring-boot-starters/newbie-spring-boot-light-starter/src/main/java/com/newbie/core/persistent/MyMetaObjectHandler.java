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

package com.newbie.core.persistent;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.newbie.context.NewBieBootEnvUtil;
import com.newbie.core.utils.Utils;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @Author: halower
 * @Date: 2019/7/4 10:45
 */
@Log4j2
@Configuration
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("cjsj", new Date(), metaObject);
        this.setFieldValByName("zhxgsj", new Date(), metaObject);
        this.setFieldValByName("sfsc", "N", metaObject);
        this.setFieldValByName("sjbsbh", Utils.random.getUUID(), metaObject);
        String networkId  = NewBieBootEnvUtil.getContext().getEnvironment().getProperty("application.network-id", "1");
        this.setFieldValByName("sjly", networkId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("zhxgsj", new Date(), metaObject);
        this.setFieldValByName("sjbsbh", Utils.random.getUUID(), metaObject);
    }
}
