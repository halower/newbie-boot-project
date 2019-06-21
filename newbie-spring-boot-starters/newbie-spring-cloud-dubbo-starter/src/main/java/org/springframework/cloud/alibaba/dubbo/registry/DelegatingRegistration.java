
package org.springframework.cloud.alibaba.dubbo.registry;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.serviceregistry.Registration;

import java.net.URI;
import java.util.Map;

/**
 * The {@link Registration} of Dubbo uses an external of {@link ServiceInstance} instance as the delegate.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
class DelegatingRegistration implements Registration {

    private final ServiceInstance delegate;

    public DelegatingRegistration(ServiceInstance delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getServiceId() {
        return delegate.getServiceId();
    }

    @Override
    public String getHost() {
        return delegate.getHost();
    }

    @Override
    public int getPort() {
        return delegate.getPort();
    }

    @Override
    public boolean isSecure() {
        return delegate.isSecure();
    }

    @Override
    public URI getUri() {
        return delegate.getUri();
    }

    @Override
    public Map<String, String> getMetadata() {
        return delegate.getMetadata();
    }

    @Override
    public String getScheme() {
        return delegate.getScheme();
    }
}
