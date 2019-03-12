package com.newbie.constants;

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
    public static final String SPRING_CLOUD_BOOTSTRAP                    = "bootstrap";
    public static final String NEWBIE_HIGH_PRIORITY_CONFIG                 = "newbieHighPriorityConfig";

    /**
     * {@link org.springframework.boot.ResourceBanner#getVersionsMap}
     */
    public static final String NEWBIE_BOOT_VERSION                         = "newbie-boot.version";
    public static final String NEWBIE_BOOT_FORMATTED_VERSION               = "newbie-boot.formatted-version";

    /**
     * resource pattern of properties file which is used to save some information of starters.
     */
    public static final String NEWBIE_BOOT_VERSION_PROPERTIES  = "classpath*:META-INF/newbie.versions.properties";

    /**
     * Default {@literal management.endpoints.web.exposure.include} value
     */
    public static final String ENDPOINTS_WEB_EXPOSURE_INCLUDE_CONFIG     = "management.endpoints.web.exposure.include";
    public static final String NEWBIE_DEFAULT_ENDPOINTS_WEB_EXPOSURE_VALUE = "info, health, versions, readiness";

    /**
     * root application context name
     */
    public static final String ROOT_APPLICATION_CONTEXT                  = "RootApplicationContext";

    /**
     * newbie configuration prefix
     */
    public static final String PREFIX                                    = "com.newbie.boot";

}
