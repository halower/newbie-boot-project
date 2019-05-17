package com.newbie.autoconfigure;

import com.newbie.core.audit.CustomAuditorAware;
import com.newbie.core.aop.ErrorControllerAdvice;
import com.newbie.core.aop.MvcConfigurer;
import com.newbie.core.aop.config.NewBieBasicConfiguration;
import com.newbie.core.persistent.config.EnableSimpleJpaRepositories;
import com.newbie.launcher.StartEventListener;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
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
@EnableJpaAuditing
@EnableSimpleJpaRepositories
public class NewBieApplicationConfiguration {
}