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
package com.newbie.initializer;


import com.newbie.core.utils.NewBieBootEnvUtils;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

@Log
public class NewBieBootInfrastructureSpringContextInitializer
                                                           implements
                                                           ApplicationContextInitializer<ConfigurableApplicationContext>,
                                                           Ordered {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        if (NewBieBootEnvUtils.isSpringCloudBootstrapEnvironment(environment)) {
            return;
        }
        log.info("NewBieBoot基础设施已经准备就绪!");
    }

    @Override
    public int getOrder() {
        //设置为最高优先级
        return HIGHEST_PRECEDENCE;
    }

}
