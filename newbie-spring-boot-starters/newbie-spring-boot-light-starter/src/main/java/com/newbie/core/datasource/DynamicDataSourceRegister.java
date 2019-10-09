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

package com.newbie.core.datasource;

import lombok.var;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册动态数据源
 * @author halower
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private DataSource defaultDataSource;

    /**
     * 用户自定义数据源
     */
    private Map<DatabaseSourceKey, DataSource> customDataSources = new HashMap<>();


    @Override
    public void setEnvironment(Environment environment) {
        defaultDataSource = buildDataSource(environment, "default");
        var writeDataSource = buildDataSource(environment, "write");
        customDataSources.put(DatabaseSourceKey.WRITE, writeDataSource);
        var readDataSource = buildDataSource(environment, "read");
        customDataSources.put(DatabaseSourceKey.READ, readDataSource);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        Map<DatabaseSourceKey, Object> targetDataSources = new HashMap<>();

        targetDataSources.put(DatabaseSourceKey.DEFAULT, this.defaultDataSource);
        DynamicDataSourceContextHolder.databaseSourceKeys.add(DatabaseSourceKey.DEFAULT);

        targetDataSources.putAll(customDataSources);
        for (DatabaseSourceKey key : customDataSources.keySet()) {
            DynamicDataSourceContextHolder.databaseSourceKeys.add(key);
        }

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);

        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);
    }


    private  DataSource buildDataSource(Environment environment, String key) {
        String prefix = "default".equals(key)? "spring.datasource": "spring.datasource."+key;
        String driverClassName  = environment.getProperty(prefix+".driver-class-name");
        String url  = environment.getProperty(prefix+".url");
        String username  = environment.getProperty(prefix+".username");
        String password  = environment.getProperty(prefix+".password");
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
    }
}
