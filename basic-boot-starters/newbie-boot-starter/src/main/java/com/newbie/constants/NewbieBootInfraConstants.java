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

    /**
     * {@link org.springframework.boot.ResourceBanner}
     */
    public static final String NEWBIE_BOOT_VERSION                         = "newbie-boot.version";
    public static final String NEWBIE_BOOT_FORMATTED_VERSION               = "newbie-boot.formatted-version";


    public static final String NEWBIE_BOOT_VERSION_PROPERTIES  = "classpath*:META-INF/newbie.versions.properties";

    /**
     * Default {@literal management.endpoints.web.exposure.include} value
     */
    public static final String ENDPOINTS_WEB_EXPOSURE_INCLUDE_CONFIG     = "management.endpoints.web.exposure.include";

    public static final String CURRENT_USER_INFO = "CURRENT_USER_INFO";

    public static final String READ_WRITE_DB_TYPE = "READ_WRITE_DB_TYPE";

    /**
     * 读写标记
     */
    public static final String WRITE_READ_TYPE_FLAG = "rwt";

    /**
     * 合法数据请求标记
     */
    public static final String LEGAL_DB_REQUEST = "DEFAULT,READ,WRITE";
}
