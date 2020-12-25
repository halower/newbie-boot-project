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

package com.newbie.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("application")
public class NewbieBootBasicProperties {
    private String env = "dev";

    private String networkId;

    private Boolean autoPackageReturnClass = false;


    private DatasourceTracker tracker = new DatasourceTracker();

    private Tomcat tomcat = new Tomcat();

    private Datasource datasoure = new Datasource();

    @Data
    public static class Tomcat {
        private Boolean autoClean = false;
        private String cleanCorn;
    }

    @Data
    public static class DatasourceTracker {
        private Boolean sqlFormat = true;
        private Boolean enabled = false;
        private Boolean thorwException = true;
        private Integer elapsed = 100;
    }


    @Data
    public static class Datasource {
        private String list ="default";
    }
}
