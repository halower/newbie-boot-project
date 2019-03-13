package com.newbie.endpoint;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VersionEndpointConfiguration {
    @ConditionalOnClass(Endpoint.class)
    public static class ConditionVersionEndpointConfiguration {
        @Bean
        @ConditionalOnEnabledEndpoint
        public NewbieBootVersionEndpoint newbieBootVersionEndpoint() {
            return new NewbieBootVersionEndpoint();
        }
    }

}