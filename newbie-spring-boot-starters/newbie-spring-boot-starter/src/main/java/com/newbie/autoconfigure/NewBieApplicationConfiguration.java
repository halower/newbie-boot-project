package com.newbie.autoconfigure;

import com.newbie.core.aop.ErrorControllerAdvice;
import com.newbie.core.aop.MvcConfigurer;
import com.newbie.core.aop.config.NewBieBasicConfiguration;
import com.newbie.core.audit.CustomAuditorAware;
import com.newbie.launcher.StartEventListener;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableConfigurationProperties({
        NewBieBasicConfiguration.class
  }
)
@Import({
        StartEventListener.class,
        CustomAuditorAware.class,
        ErrorControllerAdvice.class,
        MvcConfigurer.class
})
@Configuration
@EnableSwagger2Doc
@PropertySource("classpath:/META-INF/dubbo/app-config.properties")
public class NewBieApplicationConfiguration {
}