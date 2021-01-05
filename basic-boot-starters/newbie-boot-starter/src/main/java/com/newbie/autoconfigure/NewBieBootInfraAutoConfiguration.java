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

package com.newbie.autoconfigure;

import com.newbie.core.aop.UserInfoForWebFilter;
import com.newbie.core.config.GlobalReturnConfiguration;
import com.newbie.core.config.WebFilterConfiguration;
import com.newbie.core.datasource.DynamicDataSourceRegister;
import com.newbie.core.datasource.aop.DataSourceForWebFilter;
import com.newbie.core.datasource.aop.DynamicDataSourceAspect;
import com.newbie.core.datasource.jdbctemplate.DynamicJdbcTemplateManager;
import com.newbie.core.persistent.mybaits.MyMetaObjectHandler;
import com.newbie.core.persistent.mybaits.MybatisConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;


/**
 * @Author halower
 */

@Import({
        UserInfoForWebFilter.class,
        WebFilterConfiguration.class,
        GlobalReturnConfiguration.class,
        DataSourceForWebFilter.class,
        MybatisConfig.class,
        MyMetaObjectHandler.class,
        DynamicJdbcTemplateManager.class,
        DynamicDataSourceAspect.class,
        DynamicDataSourceRegister.class
})
@Configuration
@PropertySource("classpath:/META-INF/app-config.properties")
public class NewBieBootInfraAutoConfiguration {
}



