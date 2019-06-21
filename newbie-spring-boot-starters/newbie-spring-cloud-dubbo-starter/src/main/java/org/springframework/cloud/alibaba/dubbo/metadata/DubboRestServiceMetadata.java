
package org.springframework.cloud.alibaba.dubbo.metadata;

import java.util.Objects;

/**
 * Dubbo Rest Service Metadata
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class DubboRestServiceMetadata {

    private final ServiceRestMetadata serviceRestMetadata;

    private final RestMethodMetadata restMethodMetadata;

    public DubboRestServiceMetadata(ServiceRestMetadata serviceRestMetadata, RestMethodMetadata restMethodMetadata) {
        this.serviceRestMetadata = serviceRestMetadata;
        this.restMethodMetadata = restMethodMetadata;
    }

    public ServiceRestMetadata getServiceRestMetadata() {
        return serviceRestMetadata;
    }

    public RestMethodMetadata getRestMethodMetadata() {
        return restMethodMetadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DubboRestServiceMetadata)) {
            return false;
        }
        DubboRestServiceMetadata that = (DubboRestServiceMetadata) o;
        return Objects.equals(serviceRestMetadata, that.serviceRestMetadata) &&
                Objects.equals(restMethodMetadata, that.restMethodMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceRestMetadata, restMethodMetadata);
    }
}
