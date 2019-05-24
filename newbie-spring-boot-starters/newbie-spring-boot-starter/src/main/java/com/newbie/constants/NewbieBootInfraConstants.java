package com.newbie.constants;

import com.google.common.collect.ImmutableMap;
import com.newbie.core.persistent.common.DateParameterConstructor;
import com.newbie.core.persistent.common.ParameterConstructor;
import com.newbie.core.persistent.common.TimeParameterConstructor;
import com.newbie.core.persistent.common.TimeStampParameterConstructor;
import org.apache.dubbo.config.annotation.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * NewbieBootInfraConstants
 *
 */
public class NewbieBootInfraConstants {

    /***
     * 获取应用名: 备注 @Value("${spring.application.name:@null}")
     */
    public static final String APP_NAME_KEY                              = "spring.application.name";

    public static final String NEWBIE_DEFAULT_PROPERTY_SOURCE              = "newbieConfigurationProperties";
    public static final String NEWBIE_BOOTSTRAP                            = "newbieBootstrap";

    /**
     * {@link org.springframework.boot.ResourceBanner#getVersionsMap}
     */
    public static final String NEWBIE_BOOT_VERSION                         = "newbie-boot.version";
    public static final String NEWBIE_BOOT_FORMATTED_VERSION               = "newbie-boot.formatted-version";


    public static final String NEWBIE_BOOT_VERSION_PROPERTIES  = "classpath*:META-INF/newbie.versions.properties";

    /**
     * Default {@literal management.endpoints.web.exposure.include} value
     */
    public static final String ENDPOINTS_WEB_EXPOSURE_INCLUDE_CONFIG     = "management.endpoints.web.exposure.include";
    public static final String NEWBIE_DEFAULT_ENDPOINTS_WEB_EXPOSURE_VALUE = "info, health, versions, readiness";


    public static final Map<String, ParameterConstructor> DATE_PARAMETER_PROCESSOR = ImmutableMap.<String, ParameterConstructor>builder()
            .put("DATE", new DateParameterConstructor())
            .put("TIME", new TimeParameterConstructor())
            .put("TIMESTAMP", new TimeStampParameterConstructor()).build();

}
