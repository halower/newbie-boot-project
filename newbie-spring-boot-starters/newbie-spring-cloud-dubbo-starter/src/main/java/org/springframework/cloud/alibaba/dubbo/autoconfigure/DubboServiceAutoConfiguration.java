
package org.springframework.cloud.alibaba.dubbo.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.alibaba.dubbo.env.DubboCloudProperties;
import org.springframework.cloud.alibaba.dubbo.service.DubboGenericServiceExecutionContextFactory;
import org.springframework.cloud.alibaba.dubbo.service.DubboGenericServiceFactory;
import org.springframework.cloud.alibaba.dubbo.service.parameter.PathVariableServiceParameterResolver;
import org.springframework.cloud.alibaba.dubbo.service.parameter.RequestBodyServiceParameterResolver;
import org.springframework.cloud.alibaba.dubbo.service.parameter.RequestHeaderServiceParameterResolver;
import org.springframework.cloud.alibaba.dubbo.service.parameter.RequestParamServiceParameterResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

/**
 * Spring Boot Auto-Configuration class for Dubbo Service
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
@Configuration
@EnableConfigurationProperties(DubboCloudProperties.class)
public class DubboServiceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DubboGenericServiceFactory dubboGenericServiceFactory() {
        return new DubboGenericServiceFactory();
    }

    @Configuration
    @Import(value = {
            DubboGenericServiceExecutionContextFactory.class,
            RequestParamServiceParameterResolver.class,
            RequestBodyServiceParameterResolver.class,
            RequestHeaderServiceParameterResolver.class,
            PathVariableServiceParameterResolver.class
    })
    static class ParameterResolversConfiguration {
    }

    /**
     * Build a primary {@link PropertyResolver} bean to {@link Autowired @Autowired}
     *
     * @param environment {@link Environment}
     * @return alias bean for {@link Environment}
     */
    @Bean
    @Primary
    public PropertyResolver primaryPropertyResolver(Environment environment) {
        return environment;
    }
}