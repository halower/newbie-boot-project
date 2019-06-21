
package org.springframework.cloud.alibaba.dubbo.registry.event;

import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

import java.util.EventObject;

/**
 * The after-{@link ServiceRegistry#register(Registration) register} event for {@link Registration}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class ServiceInstanceRegisteredEvent extends EventObject {

    public ServiceInstanceRegisteredEvent(Registration source) {
        super(source);
    }

    @Override
    public Registration getSource() {
        return (Registration) super.getSource();
    }
}
