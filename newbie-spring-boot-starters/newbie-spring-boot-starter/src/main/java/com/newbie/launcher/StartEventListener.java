package com.newbie.launcher;

import com.newbie.context.NewBieBootEnvUtils;
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
 * @Author: 谢海龙
 * @Date: 2019/6/21 9:27
 * @Description
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
        NewBieBootEnvUtils.setApplicationContext(context);
        if (ClassUtils.isPresent("springfox.documentation.spring.web.plugins.Docket", null)) {
            System.out.println(String.format("API文档地址:http://localhost:%s/swagger-ui.html", localPort));
        }
        System.out.println(String.format("服务[%s]启动完成，当前使用的端口:[%s]，环境变量:[%s]", appName, localPort, profile));
    }
}
