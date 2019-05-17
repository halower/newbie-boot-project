package com.newbie.launcher;

import com.newbie.core.utils.env.NewBieBootEnvUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

/**
 * @Author: 谢海龙
 * @Date: 2019/5/14 9:27
 * @Description
 */
@Log4j2
@Configuration
public class StartEventListener {
    @Autowired
    ApplicationContext context;
    @Async
    @Order
    @EventListener(WebServerInitializedEvent.class)
    public void afterStart(WebServerInitializedEvent event) {
        Environment environment = event.getApplicationContext().getEnvironment();
        String appName = environment.getProperty("spring.application.name");
        int localPort = event.getWebServer().getPort();
        String profile = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
        NewBieBootEnvUtils.setApplicationContext(context);
        log.info("\n服务[{}]启动完成，当前使用的端口:[{}]，环境变量:[{}]", appName, localPort, profile);
    }
}
