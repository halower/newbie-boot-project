package com.newbie.launcher;

import com.newbie.context.NewbieBootContext;
import com.newbie.core.datasource.jdbctemplate.JdbcTemplateManager;
import com.newbie.core.util.net.NetUtil;
import com.newbie.core.util.text.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Log4j2
public class NewbieBootRunListener implements SpringApplicationRunListener {
    @Autowired(required = false)
    private JdbcTemplateManager jdbcTemplateManager;
    private StopWatch clock;
    private String applicationName;
    public NewbieBootRunListener(SpringApplication application, String[] args) {
        clock = new StopWatch();
    }

    @Override
    public void starting() {
        clock.start();
        System.out.println("程序即将启动");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        System.out.println("环境变量准备完成");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("上下文准备完成");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("上下文加载完成");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        clock.stop();
        final String baseDir = context.getEnvironment().getProperty("server.tomcat.basedir");
        final Boolean autoClean = Boolean.parseBoolean(
                StringUtil.isNullOrEmpty(context.getEnvironment().getProperty("application.tomcat.autoClean"))? "false":
                context.getEnvironment().getProperty("application.tomcat.auto-clean"));
        if (!StringUtil.isNullOrEmpty(baseDir) && autoClean) {
            File targetFile = new File(baseDir);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
                System.out.println("Tomcat创建临时文件目录成功");
            }
        }
        applicationName = context.getEnvironment().getProperty("spring.application.name");
        String localPort = context.getEnvironment().getProperty("server.port");
        localPort = StringUtil.isNullOrEmpty(localPort) ? "8080" : localPort;
        String profile = StringUtil.arrayToCommaDelimitedString(context.getEnvironment().getActiveProfiles());
        if (ClassUtils.isPresent("springfox.documentation.spring.web.plugins.Docket", null)) {
            String start_description = String.format(
                    "-本地API文档地址:http://localhost:%s/swagger-ui.html\n" +
                    "-局域网API文档地址:http://%s:%s/swagger-ui.html\n" +
                    "服务[%s]启动完成，当前使用的端口:[%s]，环境变量:[%s]\n" +
                    "本次启动共耗时: [%s]",
                    localPort, NetUtil.getLocalHost(), localPort, applicationName, localPort, profile, clock.getTime(TimeUnit.MILLISECONDS) / 1000 + " 秒");
            System.out.println( start_description);
            NewbieBootContext.appIsStarted = true;
            if(null != jdbcTemplateManager) {
                jdbcTemplateManager.init();
            }
        }
    }


    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("                      =========程序【" + applicationName +"】启动失败,异常信息如下=========                      \r\n" + exception);
        log.error("", exception);
    }
}
