
package org.springframework.cloud.alibaba.dubbo.registry;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.cloud.alibaba.dubbo.registry.event.ServiceInstancePreRegisteredEvent;
import org.springframework.cloud.alibaba.dubbo.registry.event.ServiceInstanceRegisteredEvent;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Dubbo Service Registration Event-Publishing Aspect
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see ServiceInstancePreRegisteredEvent
 * @see ServiceInstanceRegisteredEvent
 */
@Aspect
public class DubboServiceRegistrationEventPublishingAspect implements ApplicationEventPublisherAware {

    /**
     * The pointcut expression for {@link ServiceRegistry#register(Registration)}
     */
    public static final String REGISTER_POINTCUT_EXPRESSION =
            "execution(* org.springframework.cloud.client.serviceregistry.ServiceRegistry.register(*)) && args(registration)";

    private ApplicationEventPublisher applicationEventPublisher;

    @Before(REGISTER_POINTCUT_EXPRESSION)
    public void beforeRegister(Registration registration) {
        applicationEventPublisher.publishEvent(new ServiceInstancePreRegisteredEvent(registration));
    }

    @After(REGISTER_POINTCUT_EXPRESSION)
    public void afterRegister(Registration registration) {
        applicationEventPublisher.publishEvent(new ServiceInstanceRegisteredEvent(registration));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
