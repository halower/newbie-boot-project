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

import com.zaxxer.hikari.HikariDataSource;
import io.netty.util.internal.StringUtil;
import lombok.var;
import org.apache.dubbo.common.utils.StringUtils;
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
 *
 * @Author halower
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


    private DataSource buildDataSource(Environment environment, String key) {
        String prefix = "default".equals(key) ? "spring.datasource" : "spring.datasource." + key;
        String driverClassName = environment.getProperty(prefix + ".driver-class-name");
        String url = environment.getProperty(prefix + ".url");
        String username = environment.getProperty(prefix + ".username");
        String password = environment.getProperty(prefix + ".password");

        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();

        HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
        String minimum_idle = environment.getProperty(prefix + ".hikari.minimum-idle");
        String maximum_pool_size = environment.getProperty(prefix + ".hikari.maximum-pool-size");
        String connection_init_sql = environment.getProperty(prefix + ".hikari.connection-init-sql");
        String connection_test_query = environment.getProperty(prefix + ".hikari.connection-test-query");
        String max_lifetime = environment.getProperty(prefix + ".hikari.max-lifetime");
        String leak_detection_threshold = environment.getProperty(prefix + ".hikari.leak-detection-threshold");
        String idle_timeout = environment.getProperty(prefix + ".hikari.idle-timeout");
        String allow_pool_suspension = environment.getProperty(prefix + ".hikari.allow-pool-suspension");
        String initialization_fail_timeout = environment.getProperty(prefix + ".hikari.initialization-fail-timeout");
        String is_auto_commit = environment.getProperty(prefix + ".hikari.is-auto-commit");
        String is_register_mbeans = environment.getProperty(prefix + ".hikari.is-register-mbeans");
        String validation_timeout = environment.getProperty(prefix + ".hikari.validation-timeout");
        String pool_name = environment.getProperty(prefix + ".hikari.pool-name");

        if(!StringUtil.isNullOrEmpty(minimum_idle) && StringUtils.isInteger(minimum_idle)) {
            hikariDataSource.setMinimumIdle(Integer.parseInt(minimum_idle));
        }

        if(!StringUtil.isNullOrEmpty(pool_name)) {
            hikariDataSource.setPoolName(pool_name);
        }

        if(!StringUtil.isNullOrEmpty(validation_timeout) && StringUtils.isInteger(validation_timeout)) {
            hikariDataSource.setValidationTimeout(Integer.parseInt(validation_timeout));
        }

        if(!StringUtil.isNullOrEmpty(maximum_pool_size) && StringUtils.isInteger(maximum_pool_size)) {
            hikariDataSource.setMaximumPoolSize(Integer.parseInt(maximum_pool_size));
        }

        if(!StringUtil.isNullOrEmpty(connection_init_sql)) {
            hikariDataSource.setConnectionInitSql(connection_init_sql);
        }

        if(!StringUtil.isNullOrEmpty(connection_test_query)) {
            hikariDataSource.setConnectionInitSql(connection_test_query);
        }

        if(!StringUtil.isNullOrEmpty(max_lifetime) && StringUtils.isInteger(max_lifetime)) {
            hikariDataSource.setMaxLifetime(Integer.parseInt(max_lifetime));
        }

        if(!StringUtil.isNullOrEmpty(leak_detection_threshold) && StringUtils.isInteger(leak_detection_threshold)) {
            hikariDataSource.setLeakDetectionThreshold(Integer.parseInt(leak_detection_threshold));
        }
        if(!StringUtil.isNullOrEmpty(leak_detection_threshold) && StringUtils.isInteger(leak_detection_threshold)) {
            hikariDataSource.setLeakDetectionThreshold(Integer.parseInt(leak_detection_threshold));
        }

        if(!StringUtil.isNullOrEmpty(idle_timeout) && StringUtils.isInteger(idle_timeout)) {
            hikariDataSource.setIdleTimeout(Integer.parseInt(idle_timeout));
        }

        if(!StringUtil.isNullOrEmpty(initialization_fail_timeout) && StringUtils.isInteger(initialization_fail_timeout)) {
            hikariDataSource.setInitializationFailTimeout(Integer.parseInt(initialization_fail_timeout));
        }

        if(!StringUtil.isNullOrEmpty(allow_pool_suspension)) {
            hikariDataSource.setAllowPoolSuspension(Boolean.parseBoolean(allow_pool_suspension));
        }

        if(!StringUtil.isNullOrEmpty(is_auto_commit)) {
            hikariDataSource.setAutoCommit(Boolean.parseBoolean(is_auto_commit));
        }

        if(!StringUtil.isNullOrEmpty(is_register_mbeans)) {
            hikariDataSource.setRegisterMbeans(Boolean.parseBoolean(is_register_mbeans));
        }
        return hikariDataSource;
    }
}
