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

package com.newbie.core.aop.config;

import com.newbie.core.aop.UserInfoForWebFilter;
import com.newbie.core.datasource.aop.DataSourceForWebFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @Author: halower
 * @Date: 2019/5/22 14:56
 *
 */
@Component
@Configuration
public class WebFilterConfiguration {

    @Autowired
    UserInfoForWebFilter userInfoForWebFilter;

    @Autowired
    DataSourceForWebFilter dataSourceForWebFilter;

    @Bean
    public FilterRegistrationBean<UserInfoForWebFilter> userInfoForWebFilter() {
        FilterRegistrationBean<UserInfoForWebFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(userInfoForWebFilter);
        filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegBean;
    }

    @Bean
    public FilterRegistrationBean<DataSourceForWebFilter> dataSourceForWebFilter() {
        FilterRegistrationBean<DataSourceForWebFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(dataSourceForWebFilter);
        filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegBean;
    }
}
