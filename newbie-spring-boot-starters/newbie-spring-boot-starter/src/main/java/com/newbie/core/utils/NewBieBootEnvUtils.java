/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newbie.core.utils;

import com.newbie.constants.NewbieBootInfraConstants;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

public class NewBieBootEnvUtils {

    private final static String SPRING_CLOUD_MARK_NAME = "org.springframework.cloud.bootstrap.BootstrapConfiguration";

    public static boolean isSpringCloudBootstrapEnvironment(Environment environment) {
        if (environment instanceof ConfigurableEnvironment) {
            return !((ConfigurableEnvironment) environment).getPropertySources().contains(
                    NewbieBootInfraConstants.NEWBIE_BOOTSTRAP)
                   && isSpringCloud();
        }
        return false;
    }

    public static boolean isSpringCloud() {
        return ClassUtils.isPresent(SPRING_CLOUD_MARK_NAME, null);
    }
}
