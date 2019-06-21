
package org.springframework.cloud.alibaba.dubbo.registry;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.RegistryFactory;

import org.springframework.cloud.alibaba.dubbo.metadata.repository.DubboServiceMetadataRepository;
import org.springframework.cloud.alibaba.dubbo.service.DubboMetadataServiceProxy;
import org.springframework.cloud.alibaba.dubbo.util.JSONUtils;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Dubbo {@link RegistryFactory} uses Spring Cloud Service Registration abstraction, whose protocol is "spring-cloud"
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class SpringCloudRegistry extends AbstractSpringCloudRegistry {

    private final DubboServiceMetadataRepository dubboServiceMetadataRepository;

    public SpringCloudRegistry(URL url, DiscoveryClient discoveryClient,
                               DubboServiceMetadataRepository dubboServiceMetadataRepository,
                               DubboMetadataServiceProxy dubboMetadataConfigServiceProxy,
                               JSONUtils jsonUtils,
                               ScheduledExecutorService servicesLookupScheduler) {
        super(url, discoveryClient, dubboServiceMetadataRepository, dubboMetadataConfigServiceProxy, jsonUtils, servicesLookupScheduler);
        this.dubboServiceMetadataRepository = dubboServiceMetadataRepository;
    }

    @Override
    protected void doRegister0(URL url) {
        dubboServiceMetadataRepository.exportURL(url);
    }

    @Override
    protected void doUnregister0(URL url) {
        dubboServiceMetadataRepository.unexportURL(url);
    }
}
