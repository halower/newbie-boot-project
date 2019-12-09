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

import com.newbie.core.aop.config.ApplicationWebConfigurer;
import com.newbie.core.aop.config.NewBieBasicConfiguration;
import com.newbie.core.audit.CustomAuditorAware;
import com.newbie.core.exception.handler.GlobalExceptionHandler;
import com.newbie.core.persistent.mybaits.MyMetaObjectHandler;
import com.newbie.core.persistent.mybaits.MybatisConfig;
import com.newbie.launcher.StartEventListener;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author halower
 */
@EnableAsync
@EnableConfigurationProperties({
        NewBieBasicConfiguration.class
  }
)
@Import({
        StartEventListener.class,
        CustomAuditorAware.class,
        GlobalExceptionHandler.class,
        ApplicationWebConfigurer.class,
        MybatisConfig.class,
        MyMetaObjectHandler.class
})
@Configuration
@PropertySource("classpath:/META-INF/app-config.properties")
public class NewBieApplicationConfiguration {
}