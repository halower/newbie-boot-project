
package org.springframework.cloud.alibaba.dubbo.metadata.resolver;

import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.cloud.alibaba.dubbo.metadata.RestMethodMetadata;
import org.springframework.cloud.alibaba.dubbo.metadata.ServiceRestMetadata;

import java.util.Set;

/**
 * The REST config resolver
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public interface MetadataResolver {

    /**
     * Resolve the {@link ServiceRestMetadata} {@link Set set} from {@link ServiceBean}
     *
     * @param serviceBean {@link ServiceBean}
     * @return non-null {@link Set}
     */
    Set<ServiceRestMetadata> resolveServiceRestMetadata(ServiceBean serviceBean);

    /**
     * Resolve {@link RestMethodMetadata} {@link Set set} from {@link Class target type}
     *
     * @param targetType {@link Class target type}
     * @return non-null {@link Set}
     */
    Set<RestMethodMetadata> resolveMethodRestMetadata(Class<?> targetType);
}
