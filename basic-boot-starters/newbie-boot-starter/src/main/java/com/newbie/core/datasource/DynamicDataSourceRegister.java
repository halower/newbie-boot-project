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

import com.newbie.core.datasource.crypt.CustomDecryptInputDTO;
import com.newbie.core.datasource.crypt.DssDecryptService;
import com.zaxxer.hikari.HikariDataSource;
import io.netty.util.internal.StringUtil;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册动态数据源
 *
 * @Author halower
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    /**
     * 用户自定义数据源
     */
    private Map<String, DataSource> customDataSources = new HashMap<>();

    @Override
    public void setEnvironment(Environment environment) {
        final String datasourceList = environment.getProperty("application.datasource.list");
        if(StringUtils.isNotEmpty(datasourceList)) {
            String[] targetDatasource = datasourceList.split(",");
            for (String dataSource : targetDatasource) {
                DataSource ds = buildDataSource(environment, dataSource);
                customDataSources.put(StringUtils.upperCase(dataSource), ds);
            }

            for (String dataSource : targetDatasource) {
                customDataSources.put(StringUtils.upperCase(dataSource), buildDataSource(environment, dataSource));
            }
        }
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
         Boolean datasourceEnabled = Arrays.stream(registry.getBeanDefinitionNames()).anyMatch(name -> name.equals("org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"));
         if(datasourceEnabled){
            Map<String, Object> targetDataSources = new HashMap<>();
            targetDataSources.putAll(customDataSources);
            for (String key : customDataSources.keySet()) {
                if(!DynamicDataSourceContextHolder.databaseSourceKeys.contains(key)){
                    DynamicDataSourceContextHolder.databaseSourceKeys.add(key);
                }
            }

            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(DynamicDataSource.class);
            beanDefinition.setSynthetic(true);

            MutablePropertyValues mpv = beanDefinition.getPropertyValues();
            mpv.addPropertyValue("defaultTargetDataSource", customDataSources.get("DEFAULT"));
            mpv.addPropertyValue("targetDataSources", targetDataSources);
            registry.registerBeanDefinition("dataSource", beanDefinition);
        }
    }


    @SneakyThrows
    public DataSource buildDataSource(Environment environment, String key) {
        Integer DATABASE_URL_ENCRYPTED = 1;
        Integer DATABASE_TRACKERD = 2;
        Integer DATABASE_URL_ENCRYPTED_AND_DATABASE_TRACKERD = DATABASE_URL_ENCRYPTED | DATABASE_TRACKERD;
        Integer CURRENT_DATASOURCE_MODEL = 0;
        final String dataSourceTrackedProp = environment.getProperty("application.tracker.enabled");
        final String datasourceProp = environment.getProperty("spring.datasource.url");
        final String datasourceTrackedProp = environment.getProperty("spring.datasource.tracker-url");
        String prefix = "default".equals(key) ? "spring.datasource" : "spring.datasource." + key;
        Boolean dataSourceTracked = Boolean.parseBoolean(StringUtils.isNotEmpty(dataSourceTrackedProp)? dataSourceTrackedProp : "false");
        Boolean dataSourceUrlEncrypted; //数据源是否加密
        if(dataSourceTracked) { //开启数据源监控
            CURRENT_DATASOURCE_MODEL|= DATABASE_TRACKERD;
            dataSourceUrlEncrypted = StringUtils.isEmpty(datasourceTrackedProp)? false:  datasourceTrackedProp.contains("jdbc");
            CURRENT_DATASOURCE_MODEL |= dataSourceUrlEncrypted ? CURRENT_DATASOURCE_MODEL | DATABASE_URL_ENCRYPTED : 0;
        } else {
            dataSourceUrlEncrypted = StringUtils.isEmpty(datasourceProp)? false:  datasourceProp.contains("jdbc");
            CURRENT_DATASOURCE_MODEL |= dataSourceUrlEncrypted ? CURRENT_DATASOURCE_MODEL | DATABASE_URL_ENCRYPTED : 0;
        }
        String driverClassName = environment.getProperty(prefix + ".driver-class-name");
        String url = environment.getProperty(prefix + ".url");
        String username = environment.getProperty(prefix + ".username");
        String password = environment.getProperty(prefix + ".password");
        if(CURRENT_DATASOURCE_MODEL == DATABASE_TRACKERD) {
            driverClassName = environment.getProperty(prefix + ".tracker-driver-class-name");
            url = environment.getProperty(prefix + ".tracker-url");
            username = environment.getProperty(prefix + ".tracker-username");
            password = environment.getProperty(prefix + ".tracker-password");
        }
        if(CURRENT_DATASOURCE_MODEL == DATABASE_URL_ENCRYPTED) {
            HashMap<String,String> dbMetaInfo = decrypt(environment,prefix + ".url",prefix + ".username",prefix + ".password");
            url = dbMetaInfo.get("url");
            username = dbMetaInfo.get("username");
            password = dbMetaInfo.get("password");
        }

        if(CURRENT_DATASOURCE_MODEL == DATABASE_URL_ENCRYPTED_AND_DATABASE_TRACKERD) {
            driverClassName = environment.getProperty(prefix + ".tracker-driver-class-name");
            HashMap<String,String> dbMetaInfo = decrypt(environment,prefix + ".tracker-url",prefix + ".tracker-username",prefix + ".tracker-password");
            url = dbMetaInfo.get("url");
            username = dbMetaInfo.get("username");
            password = dbMetaInfo.get("password");
        }

        if(StringUtil.isNullOrEmpty(url) || StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password)) {
            throw new NotFoundException(String.format("数据源{%s}并不存在,请检查application.datasource.list配置是否合理", key));
        }

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
        if(!StringUtil.isNullOrEmpty(minimum_idle)) {
            hikariDataSource.setMinimumIdle(Integer.parseInt(minimum_idle));
        }

        if(!StringUtil.isNullOrEmpty(pool_name)) {
            hikariDataSource.setPoolName(pool_name);
        }

        if(!StringUtil.isNullOrEmpty(validation_timeout)) {
            hikariDataSource.setValidationTimeout(Integer.parseInt(validation_timeout));
        }

        if(!StringUtil.isNullOrEmpty(maximum_pool_size)) {
            hikariDataSource.setMaximumPoolSize(Integer.parseInt(maximum_pool_size));
        }

        if(!StringUtil.isNullOrEmpty(connection_init_sql)) {
            hikariDataSource.setConnectionInitSql(connection_init_sql);
        }

        if(!StringUtil.isNullOrEmpty(connection_test_query)) {
            hikariDataSource.setConnectionTestQuery(connection_test_query);
        }

        if(!StringUtil.isNullOrEmpty(max_lifetime)) {
            hikariDataSource.setMaxLifetime(Integer.parseInt(max_lifetime));
        }

        if(!StringUtil.isNullOrEmpty(leak_detection_threshold)) {
            hikariDataSource.setLeakDetectionThreshold(Integer.parseInt(leak_detection_threshold));
        }
        if(!StringUtil.isNullOrEmpty(leak_detection_threshold)) {
            hikariDataSource.setLeakDetectionThreshold(Integer.parseInt(leak_detection_threshold));
        }

        if(!StringUtil.isNullOrEmpty(idle_timeout) ) {
            hikariDataSource.setIdleTimeout(Integer.parseInt(idle_timeout));
        }

        if(!StringUtil.isNullOrEmpty(initialization_fail_timeout)) {
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


    public HashMap<String,String>  decrypt(Environment environment, String url,String username, String password) {
        HashMap map = new HashMap();
        map.put("url", environment.getProperty(url));
        map.put("username", environment.getProperty(username));
        map.put("password",  environment.getProperty(password));

        final CustomDecryptInputDTO customDecryptInputDTO = new CustomDecryptInputDTO();
        customDecryptInputDTO.setDecryptMap(map);
        Map decryptMap = new DssDecryptService().decrypt(environment,customDecryptInputDTO);
        HashMap<String,String> ret = new HashMap();
        ret.put("url",decryptMap.get("url").toString());
        ret.put("username",decryptMap.get("username").toString());
        ret.put("password",decryptMap.get("password").toString());
        return  ret;
    }

    public static boolean is(int current, int type) {
        int actualType = 0 << type;
        return (current & actualType) == actualType;
    }
}



