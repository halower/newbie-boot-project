
package org.springframework.cloud.alibaba.dubbo.context;

import org.springframework.cloud.alibaba.dubbo.registry.SpringCloudRegistryFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * The Dubbo services will be registered as the specified Spring cloud applications that will not be considered
 * normal ones, but only are used to Dubbo's service discovery even if it is based on Spring Cloud Commons abstraction.
 * However, current application will be registered by other DiscoveryClientAutoConfiguration.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class DubboServiceRegistrationApplicationContextInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // Set ApplicationContext into SpringCloudRegistryFactory before Dubbo Service Register
        SpringCloudRegistryFactory.setApplicationContext(applicationContext);
    }
}
