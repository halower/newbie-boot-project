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

package com.newbie.context;


import com.newbie.constants.NewbieBootInfraConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MissingRequiredPropertiesException;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.StringUtils;

import java.util.Properties;

@Configuration
public class EnvironmentCustomizer implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        /**
         * Get NewbieBoot version properties
         */
        Properties defaultConfiguration = getNewbieBootVersionProperties();

        /**
         * Config default value of {@literal management.endpoints.web.exposure.include}
         */
        defaultConfiguration.put(NewbieBootInfraConstants.ENDPOINTS_WEB_EXPOSURE_INCLUDE_CONFIG,
                NewbieBootInfraConstants.ENDPOINTS_WEB_EXPOSURE_INCLUDE_CONFIG);

        PropertiesPropertySource propertySource = new PropertiesPropertySource(
        NewbieBootInfraConstants.NEWBIE_DEFAULT_PROPERTY_SOURCE, defaultConfiguration);
        environment.getPropertySources().addLast(propertySource);

        /**
         * set required properties, {@link MissingRequiredPropertiesException}
         **/
        if (NewBieBootEnvUtil.isSpringCloudBootstrapEnvironment(environment)) {
            environment.setRequiredProperties(NewbieBootInfraConstants.APP_NAME_KEY);
        }
    }

    /**
     * {@link org.springframework.boot.ResourceBanner#getVersionsMap}
     */
    private Properties getNewbieBootVersionProperties() {
        Properties properties = new Properties();
        String newbieBootVersion = EnvironmentCustomizer.class.getPackage().getImplementationVersion();
        newbieBootVersion = StringUtils.isEmpty(newbieBootVersion) ? "" : newbieBootVersion;
        String newbieBootFormattedVersion = newbieBootVersion.isEmpty() ? "" : String.format(" (v%s)", newbieBootVersion);
        properties.setProperty(NewbieBootInfraConstants.NEWBIE_BOOT_VERSION, newbieBootVersion);
        properties.setProperty(NewbieBootInfraConstants.NEWBIE_BOOT_FORMATTED_VERSION, newbieBootFormattedVersion);
        return properties;
    }
}