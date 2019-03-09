package com.newbie.starter;

import com.newbie.core.aop.ErrorControllerAdvice;
import com.newbie.core.aop.config.NewBieBasicConfig;
import com.newbie.core.config.MvcConfigurer;
import com.newbie.core.storage.StorageController;
import com.newbie.core.storage.config.StorageConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableAsync
@EnableCaching
@SpringBootConfiguration
@EnableAutoConfiguration
@EnableConfigurationProperties({StorageConfig.class, NewBieBasicConfig.class})
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)
})

@Import({
        ErrorControllerAdvice.class,
        StorageController.class,
        MvcConfigurer.class
})
public @interface NewBieBootApplication {
}
