package com.newbie.autoconfigure;

//import com.newbie.core.aop.ErrorControllerAdvice;
import com.newbie.core.aop.ErrorControllerAdvice;
import com.newbie.core.aop.MvcConfigurer;
import com.newbie.core.aop.config.NewBieBasicConfiguration;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@EnableConfigurationProperties({
        NewBieBasicConfiguration.class
  }
)
@Import({
        ErrorControllerAdvice.class,
        MvcConfigurer.class
})
@Configuration
@EnableSwagger2Doc
public class NewBieApplicationConfiguration {
}