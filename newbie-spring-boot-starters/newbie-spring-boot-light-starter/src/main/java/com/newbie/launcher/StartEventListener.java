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

package com.newbie.launcher;

import com.newbie.context.NewBieBootEnvUtil;
import com.newbie.core.utils.Utils;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * @Author: halower
 * @Date: 2019/6/21 9:27
 *
 */
@Configuration
public class StartEventListener {
    @Autowired
    ApplicationContext context;

    @Async
    @Order(Ordered.LOWEST_PRECEDENCE -1)
    @EventListener(WebServerInitializedEvent.class)
    public void afterStart(WebServerInitializedEvent event) {
        Environment environment = event.getApplicationContext().getEnvironment();
        String appName = environment.getProperty("spring.application.name");
        int localPort = event.getWebServer().getPort();
        String profile = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
        NewBieBootEnvUtil.setApplicationContext(context);
        String networkFlag = environment.getProperty("application.network-flag");
        if( StringUtils.isEmpty(networkFlag)) {
            networkFlag = "1";
        }
        NewBieBootEnvUtil.setNetworkFalg(networkFlag);
        if (ClassUtils.isPresent("springfox.documentation.spring.web.plugins.Docket", null)) {
            var runInfo =  String.format(
                    "-本地 API文档地址:http://localhost:%s/swagger-ui.html\n" +
                            "-局域网 API文档地址:http://%s:%s/swagger-ui.html\n" +
                            "服务[%s]启动完成，当前使用的端口:[%s]，环境变量:[%s]",
                    localPort, Utils.network.getHostIp(),localPort, appName, localPort, profile);
            System.out.println(runInfo);
        }
    }
}
