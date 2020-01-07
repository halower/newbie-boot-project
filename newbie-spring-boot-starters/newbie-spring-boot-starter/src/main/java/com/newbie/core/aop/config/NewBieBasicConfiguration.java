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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "application")
public class NewBieBasicConfiguration {
    private String env = "dev";

    private String networkId;

    private Boolean autoPackageReturnClass = false;

    private String authSecretKey="1b6ba62cff8629865a6b9dffa1586d74";

    private DubboConfig dubbo;

    private DatasourceTracker tracker;

    @Data
    public static class DatasourceTracker {
        private Boolean sqlFormat = true;
        private Boolean enabled = false;
        private Boolean thorwException = true;
        private Integer elapsed = 100;
    }

    @Data
    public static class DubboConfig {
        private ConsumerConfig consumer;
        private ProviderConfig provider;

        @Data
        public static class ProviderConfig {
            private String version ="1.0.0";
        }

        @Data
        public static class ConsumerConfig {
            private String version ="1.0.0";
        }
    }
}
