
package org.springframework.cloud.alibaba.dubbo.service.parameter;

import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.cloud.alibaba.dubbo.http.HttpServerRequest;
import org.springframework.cloud.alibaba.dubbo.metadata.MethodParameterMetadata;
import org.springframework.cloud.alibaba.dubbo.metadata.RestMethodMetadata;
import org.springframework.core.Ordered;

/**
 * Dubbo {@link GenericService} Parameter Resolver
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public interface DubboGenericServiceParameterResolver extends Ordered {

    /**
     * Resolves a method parameter into an argument value from a given request.
     *
     * @return
     */
    Object resolve(RestMethodMetadata restMethodMetadata, MethodParameterMetadata methodParameterMetadata,
                   HttpServerRequest request);

    Object resolve(RestMethodMetadata restMethodMetadata, MethodParameterMetadata methodParameterMetadata,
                   RestMethodMetadata clientRestMethodMetadata, Object[] arguments);
}
