package com.newbie.core.aop.config;

import com.newbie.core.aop.UserInfoForWebFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @Author: 谢海龙
 * @Date: 2019/5/22 14:56
 * @Description
 */
@Configuration
@Component
public class WebFilterConfiguration {
    @Autowired
    UserInfoForWebFilter userInfoForWebFilter;

    @Bean
    public FilterRegistrationBean<UserInfoForWebFilter> userInfoForWebFilter() {
        FilterRegistrationBean<UserInfoForWebFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(userInfoForWebFilter);
        filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegBean;
    }
}
