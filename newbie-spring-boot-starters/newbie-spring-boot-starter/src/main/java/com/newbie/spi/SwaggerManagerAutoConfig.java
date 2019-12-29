package com.newbie.spi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SwaggerManagerAutoConfig {
    @Bean
    public SpiFactoryBean swaggerManagerProxy(ApplicationContext applicationContext) {
        return new SpiFactoryBean(applicationContext, SwaggerManager.class);
    }

    @Bean
    @Primary
    public SwaggerManager swaggerManagerProxy(SpiFactoryBean spiFactoryBean) throws Exception {
        return (SwaggerManager) spiFactoryBean.getObject();
    }
}