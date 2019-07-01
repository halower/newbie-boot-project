/*
 * 版权所有 (c) 2019-2029, halower (halower@foxmail.com).
 *
 * Apache 2.0 License 同时该协议为补充协议，不允许 996 工作制度企业使用该开源软件
 *
 * 反996许可证版本1.0
 *
 * 在符合下列条件的情况下，特此免费向任何得到本授权作品的副本（包括源代码、文件和/或相关内容，以下
 * 统称为“授权作品”）的个人和法人实体授权：被授权个人或法人实体有权以任何目的处置授权作品，包括但
 * 不限于使用、复制，修改，衍生利用、散布，发布和再许可：
 *
 * 1. 个人或法人实体必须在许可作品的每个再散布或衍生副本上包含以上版权声明和本许可证，不得自行修
 * 改。
 * 2. 个人或法人实体必须严格遵守与个人实际所在地或个人出生地或归化地、或法人实体注册地或经营地
 * （以较严格者为准）的司法管辖区所有适用的与劳动和就业相关法律、法规、规则和标准。如果该司法管辖
 * 区没有此类法律、法规、规章和标准或其法律、法规、规章和标准不可执行，则个人或法人实体必须遵守国
 * 际劳工标准的核心公约。
 * 3. 个人或法人不得以任何方式诱导、暗示或强迫其全职或兼职员工或其独立承包人以口头或书面形式同意直接或
 * 间接限制、削弱或放弃其所拥有的，受相关与劳动和就业有关的法律、法规、规则和标准保护的权利或补救
 * 措施，无论该等书面或口头协议是否被该司法管辖区的法律所承认，该等个人或法人实体也不得以任何方法
 * 限制其雇员或独立承包人向版权持有人或监督许可证合规情况的有关当局报告或投诉上述违反许可证的行为
 * 的权利。
 *
 * 该授权作品是"按原样"提供，不做任何明示或暗示的保证，包括但不限于对适销性、特定用途适用性和非侵
 * 权性的保证。在任何情况下，无论是在合同诉讼、侵权诉讼或其他诉讼中，版权持有人均不承担因本软件或
 * 本软件的使用或其他交易而产生、引起或与之相关的任何索赔、损害或其他责任。
 */
package org.springframework.cloud.alibaba.dubbo.autoconfigure;

import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.ServiceBean;

import com.ecwid.consul.v1.agent.model.NewService;
import com.netflix.appinfo.InstanceInfo;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.alibaba.dubbo.autoconfigure.condition.MissingSpringCloudRegistryConfigPropertyCondition;
import org.springframework.cloud.alibaba.dubbo.metadata.repository.DubboServiceMetadataRepository;
import org.springframework.cloud.alibaba.dubbo.registry.DubboServiceRegistrationEventPublishingAspect;
import org.springframework.cloud.alibaba.dubbo.registry.event.ServiceInstancePreRegisteredEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaAutoServiceRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.alibaba.dubbo.autoconfigure.DubboServiceRegistrationAutoConfiguration.CONSUL_AUTO_CONFIGURATION_CLASS_NAME;
import static org.springframework.cloud.alibaba.dubbo.autoconfigure.DubboServiceRegistrationAutoConfiguration.EUREKA_AUTO_CONFIGURATION_CLASS_NAME;
import static org.springframework.cloud.alibaba.dubbo.registry.SpringCloudRegistryFactory.ADDRESS;
import static org.springframework.cloud.alibaba.dubbo.registry.SpringCloudRegistryFactory.PROTOCOL;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Dubbo Service Registration Auto-{@link Configuration}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
@Configuration
@Import({DubboServiceRegistrationEventPublishingAspect.class})
@ConditionalOnProperty(value = "spring.cloud.service-registry.auto-registration.enabled", matchIfMissing = true)
@AutoConfigureAfter(name = {
        EUREKA_AUTO_CONFIGURATION_CLASS_NAME,
        CONSUL_AUTO_CONFIGURATION_CLASS_NAME,
        "org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationAutoConfiguration"
}, value = {
        DubboMetadataAutoConfiguration.class
})
public class DubboServiceRegistrationAutoConfiguration {

    public static final String EUREKA_AUTO_CONFIGURATION_CLASS_NAME =
            "org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration";

    public static final String CONSUL_AUTO_CONFIGURATION_CLASS_NAME =
            "org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistrationAutoConfiguration";

    public static final String CONSUL_AUTO_REGISTRATION_CLASS_NAME =
            "org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration";

    public static final String ZOOKEEPER_AUTO_CONFIGURATION_CLASS_NAME =
            "org.springframework.cloud.zookeeper.serviceregistry.ZookeeperAutoServiceRegistrationAutoConfiguration";

    private static final Logger logger = LoggerFactory.getLogger(DubboServiceRegistrationAutoConfiguration.class);

    @Autowired
    private DubboServiceMetadataRepository dubboServiceMetadataRepository;

    @Bean
    @Conditional(value = {
            MissingSpringCloudRegistryConfigPropertyCondition.class
    })
    public RegistryConfig defaultSpringCloudRegistryConfig() {
        return new RegistryConfig(ADDRESS, PROTOCOL);
    }

    @EventListener(ServiceInstancePreRegisteredEvent.class)
    public void onServiceInstancePreRegistered(ServiceInstancePreRegisteredEvent event) {
        Registration registration = event.getSource();
        attachDubboMetadataServiceMetadata(registration);
    }

    @Configuration
    @ConditionalOnBean(name = EUREKA_AUTO_CONFIGURATION_CLASS_NAME)
    @Aspect
    class EurekaConfiguration implements SmartInitializingSingleton {

        @Autowired
        private ObjectProvider<Collection<ServiceBean>> serviceBeans;

        @EventListener(ServiceInstancePreRegisteredEvent.class)
        public void onServiceInstancePreRegistered(ServiceInstancePreRegisteredEvent event) {
            Registration registration = event.getSource();
            EurekaRegistration eurekaRegistration = EurekaRegistration.class.cast(registration);
            InstanceInfo instanceInfo = eurekaRegistration.getApplicationInfoManager().getInfo();
            attachDubboMetadataServiceMetadata(instanceInfo.getMetadata());
        }

        /**
         * {@link EurekaServiceRegistry} will register current {@link ServiceInstance service instance} on
         * {@link EurekaAutoServiceRegistration#start()} execution(in {@link SmartLifecycle#start() start phase}),
         * thus this method must {@link ServiceBean#export() export} all {@link ServiceBean ServiceBeans} in advance.
         */
        @Override
        public void afterSingletonsInstantiated() {
            Collection<ServiceBean> serviceBeans = this.serviceBeans.getIfAvailable();
            if (!isEmpty(serviceBeans)) {
                serviceBeans.forEach(ServiceBean::export);
            }
        }
    }

    @Configuration
    @ConditionalOnBean(name = CONSUL_AUTO_CONFIGURATION_CLASS_NAME)
    @AutoConfigureOrder
    class ConsulConfiguration {

        /**
         * Handle the pre-registered event of {@link ServiceInstance} for Consul
         *
         * @param event {@link ServiceInstancePreRegisteredEvent}
         */
        @EventListener(ServiceInstancePreRegisteredEvent.class)
        public void onServiceInstancePreRegistered(ServiceInstancePreRegisteredEvent event) {
            Registration registration = event.getSource();
            Class<?> registrationClass = AopUtils.getTargetClass(registration);
            String registrationClassName = registrationClass.getName();
            if (CONSUL_AUTO_REGISTRATION_CLASS_NAME.equalsIgnoreCase(registrationClassName)) {
                ConsulRegistration consulRegistration = (ConsulRegistration) registration;
                attachURLsIntoMetadata(consulRegistration);
            }
        }

        private void attachURLsIntoMetadata(ConsulRegistration consulRegistration) {
            NewService newService = consulRegistration.getService();
            Map<String, String> serviceMetadata = dubboServiceMetadataRepository.getDubboMetadataServiceMetadata();
            if (!isEmpty(serviceMetadata)) {
                List<String> tags = newService.getTags();
                for (Map.Entry<String, String> entry : serviceMetadata.entrySet()) {
                    tags.add(entry.getKey() + "=" + entry.getValue());
                }
            }
        }
    }

    private void attachDubboMetadataServiceMetadata(Registration registration) {
        if (registration == null) {
            return;
        }
        synchronized (registration) {
            Map<String, String> metadata = registration.getMetadata();
            attachDubboMetadataServiceMetadata(metadata);
        }
    }

    private void attachDubboMetadataServiceMetadata(Map<String, String> metadata) {
        Map<String, String> serviceMetadata = dubboServiceMetadataRepository.getDubboMetadataServiceMetadata();
        if (!isEmpty(serviceMetadata)) {
            metadata.putAll(serviceMetadata);
        }
    }
}