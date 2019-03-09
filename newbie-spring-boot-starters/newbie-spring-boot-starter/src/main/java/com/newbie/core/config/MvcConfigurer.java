package com.newbie.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer {
    // 支持跨域规则
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST","PUT","UPDATE","DELETE","PATCH");
    }
    // 格式化
    @Override
    public void addFormatters(FormatterRegistry registry) {
        //转换成对应的日期格式
        registry.addFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
    }

}
