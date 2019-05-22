package com.newbie.core.aop.config;

import com.newbie.core.aop.UserInfoForWebFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author: 谢海龙
 * @Date: 2019/5/22 14:56
 * @Description
 */
@Configuration
@Component
public class WebFilterConfiguration implements WebMvcConfigurer {
    @Autowired
    UserInfoForWebFilter userInfoForWebFilter;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInfoForWebFilter);
    }
}
