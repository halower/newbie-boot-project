package com.newbie.env;


import com.newbie.constants.NewbieBootInfraConstants;
import com.newbie.autoconfigure.NewBieBootInfraAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MissingRequiredPropertiesException;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.StringUtils;

import java.util.Properties;


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
        environment.setRequiredProperties(NewbieBootInfraConstants.APP_NAME_KEY);
    }

    /**
     * {@link org.springframework.boot.ResourceBanner#getVersionsMap}
     */
    private Properties getNewbieBootVersionProperties() {
        Properties properties = new Properties();
        String newbieBootVersion = NewBieBootInfraAutoConfiguration.class.getPackage().getImplementationVersion();
        newbieBootVersion = StringUtils.isEmpty(newbieBootVersion) ? "" : newbieBootVersion;
        String newbieBootFormattedVersion = newbieBootVersion.isEmpty() ? "" : String.format(" (v%s)", newbieBootVersion);
        properties.setProperty(NewbieBootInfraConstants.NEWBIE_BOOT_VERSION, newbieBootVersion);
        properties.setProperty(NewbieBootInfraConstants.NEWBIE_BOOT_FORMATTED_VERSION, newbieBootFormattedVersion);
        return properties;
    }
}