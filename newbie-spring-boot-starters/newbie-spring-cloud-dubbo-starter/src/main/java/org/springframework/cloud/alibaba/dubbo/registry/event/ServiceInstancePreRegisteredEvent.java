
package org.springframework.cloud.alibaba.dubbo.registry.event;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationEvent;

/**
 * The before-{@link ServiceRegistry#register(Registration) register} event for {@link ServiceInstance}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class ServiceInstancePreRegisteredEvent extends ApplicationEvent {

    public ServiceInstancePreRegisteredEvent(Registration source) {
        super(source);
    }

    @Override
    public Registration getSource() {
        return (Registration) super.getSource();
    }
}
