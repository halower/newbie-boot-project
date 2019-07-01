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

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.alibaba.dubbo.annotation.DubboTransported;
import org.springframework.cloud.alibaba.dubbo.client.loadbalancer.DubboMetadataInitializerInterceptor;
import org.springframework.cloud.alibaba.dubbo.client.loadbalancer.DubboTransporterInterceptor;
import org.springframework.cloud.alibaba.dubbo.metadata.repository.DubboServiceMetadataRepository;
import org.springframework.cloud.alibaba.dubbo.metadata.resolver.DubboTransportedAttributesResolver;
import org.springframework.cloud.alibaba.dubbo.service.DubboGenericServiceExecutionContextFactory;
import org.springframework.cloud.alibaba.dubbo.service.DubboGenericServiceFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.cloud.client.loadbalancer.RetryLoadBalancerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.type.MethodMetadata;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Dubbo Auto-{@link Configuration} for {@link LoadBalanced @LoadBalanced} {@link RestTemplate}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
@Configuration
@ConditionalOnClass(name = {"org.springframework.web.client.RestTemplate"})
@AutoConfigureAfter(name = {"org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration"})
public class DubboLoadBalancedRestTemplateAutoConfiguration implements BeanClassLoaderAware, SmartInitializingSingleton {

    private static final Class<DubboTransported> DUBBO_TRANSPORTED_CLASS = DubboTransported.class;

    private static final String DUBBO_TRANSPORTED_CLASS_NAME = DUBBO_TRANSPORTED_CLASS.getName();

    @Autowired
    private DubboServiceMetadataRepository repository;

    @Autowired(required = false)
    private LoadBalancerInterceptor loadBalancerInterceptor;

    @Autowired(required = false)
    private RetryLoadBalancerInterceptor retryLoadBalancerInterceptor;

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Autowired
    private DubboGenericServiceFactory serviceFactory;

    @Autowired
    private DubboGenericServiceExecutionContextFactory contextFactory;

    @Autowired
    private Environment environment;

    @LoadBalanced
    @Autowired(required = false)
    private Map<String, RestTemplate> restTemplates = Collections.emptyMap();

    private ClassLoader classLoader;

    /**
     * The {@link ClientHttpRequestInterceptor} bean that may be {@link LoadBalancerInterceptor} or {@link RetryLoadBalancerInterceptor}
     */
    private ClientHttpRequestInterceptor loadBalancerInterceptorBean;

    @Override
    public void afterSingletonsInstantiated() {
        loadBalancerInterceptorBean = retryLoadBalancerInterceptor != null ?
                retryLoadBalancerInterceptor :
                loadBalancerInterceptor;
    }

    /**
     * Adapt the {@link RestTemplate} beans that are annotated  {@link LoadBalanced @LoadBalanced} and
     * {@link LoadBalanced @LoadBalanced} when Spring Boot application started
     * (after the callback of {@link SmartInitializingSingleton} beans or
     * {@link RestTemplateCustomizer#customize(RestTemplate) customization})
     */
    @EventListener(ApplicationStartedEvent.class)
    public void adaptRestTemplates() {

        DubboTransportedAttributesResolver attributesResolver = new DubboTransportedAttributesResolver(environment);

        for (Map.Entry<String, RestTemplate> entry : restTemplates.entrySet()) {
            String beanName = entry.getKey();
            Map<String, Object> dubboTranslatedAttributes = getDubboTranslatedAttributes(beanName, attributesResolver);
            if (!CollectionUtils.isEmpty(dubboTranslatedAttributes)) {
                adaptRestTemplate(entry.getValue(), dubboTranslatedAttributes);
            }
        }
    }

    /**
     * Gets the annotation attributes {@link RestTemplate} bean being annotated
     * {@link DubboTransported @DubboTransported}
     *
     * @param beanName           the bean name of {@link LoadBalanced @LoadBalanced} {@link RestTemplate}
     * @param attributesResolver {@link DubboTransportedAttributesResolver}
     * @return non-null {@link Map}
     */
    private Map<String, Object> getDubboTranslatedAttributes(String beanName,
                                                             DubboTransportedAttributesResolver attributesResolver) {
        Map<String, Object> attributes = Collections.emptyMap();
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
        if (beanDefinition instanceof AnnotatedBeanDefinition) {
            AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
            MethodMetadata factoryMethodMetadata = annotatedBeanDefinition.getFactoryMethodMetadata();
            attributes = factoryMethodMetadata != null ?
                    factoryMethodMetadata.getAnnotationAttributes(DUBBO_TRANSPORTED_CLASS_NAME) : Collections.emptyMap();
        }
        return attributesResolver.resolve(attributes);
    }


    /**
     * Adapt the instance of {@link DubboTransporterInterceptor} to the {@link LoadBalancerInterceptor} Bean.
     *
     * @param restTemplate              {@link LoadBalanced @LoadBalanced} {@link RestTemplate} Bean
     * @param dubboTranslatedAttributes the annotation dubboTranslatedAttributes {@link RestTemplate} bean being annotated
     *                                  {@link DubboTransported @DubboTransported}
     */
    private void adaptRestTemplate(RestTemplate restTemplate, Map<String, Object> dubboTranslatedAttributes) {

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(restTemplate.getInterceptors());

        int index = loadBalancerInterceptorBean == null ? -1 : interceptors.indexOf(loadBalancerInterceptorBean);

        index = index < 0 ? 0 : index;

        // Add ClientHttpRequestInterceptor instances before loadBalancerInterceptor
        interceptors.add(index++, new DubboMetadataInitializerInterceptor(repository));

        interceptors.add(index++, new DubboTransporterInterceptor(repository, restTemplate.getMessageConverters(),
                classLoader, dubboTranslatedAttributes, serviceFactory, contextFactory));

        restTemplate.setInterceptors(interceptors);
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
