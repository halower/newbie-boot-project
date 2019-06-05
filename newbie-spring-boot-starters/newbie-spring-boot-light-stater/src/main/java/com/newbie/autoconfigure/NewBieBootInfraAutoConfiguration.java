package com.newbie.autoconfigure;

import com.newbie.core.aop.UserInfoForDubboFilter;
import com.newbie.core.aop.UserInfoForWebFilter;
import com.newbie.core.aop.config.WebFilterConfiguration;
import com.newbie.endpoint.VersionEndpointConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        VersionEndpointConfiguration.class,
        UserInfoForWebFilter.class,
        UserInfoForDubboFilter.class,
        WebFilterConfiguration.class,
})
@Configuration
public class NewBieBootInfraAutoConfiguration {
}



