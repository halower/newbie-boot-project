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
import cn.hutool.core.net.NetUtil;
import com.newbie.context.NewbieBootContext;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * @Author: halower
 * @Date: 2019/6/21 9:27
 *
 */
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

        final String baseDir = context.getEnvironment().getProperty("server.tomcat.basedir");
        final Boolean autoClean = Boolean.parseBoolean(
                StringUtils.isEmpty(context.getEnvironment().getProperty("application.tomcat.autoClean")) ? "false" :
                        context.getEnvironment().getProperty("application.tomcat.auto-clean"));
        if (!StringUtils.isEmpty(baseDir) && autoClean) {
            File targetFile = new File(baseDir);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
                System.out.println("Tomcat创建临时文件目录成功");
            }
        }

        if (ClassUtils.isPresent("springfox.documentation.spring.web.plugins.Docket", null)) {
            var runInfo =  String.format(
                    "-本地 API文档地址:http://localhost:%s/doc.html\n" +
                            "-局域网 API文档地址:http://%s:%s/doc.html\n" +
                            "服务[%s]启动完成，当前使用的端口:[%s]，环境变量:[%s]",
                    localPort, NetUtil.getLocalhostStr(),localPort, appName, localPort, profile);
            NewbieBootContext.appIsStarted = true;
            System.out.println(runInfo);
        }
    }
}
