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
package com.newbie.core.monitor.p6spy;

import com.p6spy.engine.spy.P6ModuleManager;
import com.p6spy.engine.spy.P6SpyDriver;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.option.SystemProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.util.Map;


@Configuration
@ConditionalOnClass(P6SpyDriver.class)
@EnableConfigurationProperties(P6spyProperties.class)
@ConditionalOnProperty(prefix = P6spyProperties.P6SPY_CONFIG_PREFIX, name = "enabled", havingValue = "true",matchIfMissing = true)
public class P6spyAutoConfiguration implements ApplicationRunner {

    private final Environment environment;

    public P6spyAutoConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {
        P6spyAutoConfiguration.p6spyReload(environment);
    }

    public static void p6spyReload(Environment p6spyProperties) {
        Map<String, String> defaults = P6SpyOptions.getActiveInstance().getDefaults();
        Field[] fields = P6spyProperties.class.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String propertiesName = SystemProperties.P6SPY_PREFIX.concat(fieldName);
            if (p6spyProperties.containsProperty(propertiesName)) {
                String systemPropertyValue = p6spyProperties.getProperty(propertiesName, defaults.get(fieldName));
                defaults.put(fieldName, systemPropertyValue);
            }
        }
        P6SpyOptions.getActiveInstance().load(defaults);
        P6ModuleManager.getInstance().reload();
    }
}

