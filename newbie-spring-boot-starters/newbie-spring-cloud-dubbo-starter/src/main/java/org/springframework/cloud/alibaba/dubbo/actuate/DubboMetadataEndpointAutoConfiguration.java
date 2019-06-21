
package org.springframework.cloud.alibaba.dubbo.actuate;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.alibaba.dubbo.actuate.endpoint.DubboRestMetadataEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Dubbo Metadata Endpoints Auto-{@link Configuration}
 */
@ConditionalOnClass(name = "org.springframework.boot.actuate.endpoint.annotation.Endpoint")
@PropertySource(value = "classpath:/META-INF/dubbo/default/actuator-endpoints.properties")
@ManagementContextConfiguration
public class DubboMetadataEndpointAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnEnabledEndpoint
    public DubboRestMetadataEndpoint dubboRestMetadataEndpoint() {
        return new DubboRestMetadataEndpoint();
    }
}


