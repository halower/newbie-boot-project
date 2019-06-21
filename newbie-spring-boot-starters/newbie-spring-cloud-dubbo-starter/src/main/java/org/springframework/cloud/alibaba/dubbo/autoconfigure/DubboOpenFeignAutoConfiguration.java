
package org.springframework.cloud.alibaba.dubbo.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.alibaba.dubbo.metadata.repository.DubboServiceMetadataRepository;
import org.springframework.cloud.alibaba.dubbo.openfeign.TargeterBeanPostProcessor;
import org.springframework.cloud.alibaba.dubbo.service.DubboGenericServiceExecutionContextFactory;
import org.springframework.cloud.alibaba.dubbo.service.DubboGenericServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static org.springframework.cloud.alibaba.dubbo.autoconfigure.DubboOpenFeignAutoConfiguration.TARGETER_CLASS_NAME;


/**
 * Dubbo Feign Auto-{@link Configuration Configuration}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
@ConditionalOnClass(name = {"feign.Feign", TARGETER_CLASS_NAME})
@AutoConfigureAfter(name = {"org.springframework.cloud.openfeign.FeignAutoConfiguration"})
@Configuration
public class DubboOpenFeignAutoConfiguration {

    public static final String TARGETER_CLASS_NAME = "org.springframework.cloud.openfeign.Targeter";

    @Bean
    public TargeterBeanPostProcessor targeterBeanPostProcessor(Environment environment,
                                                               DubboServiceMetadataRepository dubboServiceMetadataRepository,
                                                               DubboGenericServiceFactory dubboGenericServiceFactory,
                                                               DubboGenericServiceExecutionContextFactory contextFactory) {
        return new TargeterBeanPostProcessor(environment, dubboServiceMetadataRepository,
                dubboGenericServiceFactory, contextFactory);
    }

}