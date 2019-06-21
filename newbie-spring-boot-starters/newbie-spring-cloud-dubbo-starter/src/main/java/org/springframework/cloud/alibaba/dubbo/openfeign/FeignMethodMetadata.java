
package org.springframework.cloud.alibaba.dubbo.openfeign;

import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.cloud.alibaba.dubbo.metadata.RestMethodMetadata;

import java.lang.reflect.Method;

/**
 * Feign {@link Method} Metadata
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
class FeignMethodMetadata {

    private final GenericService dubboGenericService;

    private final RestMethodMetadata dubboRestMethodMetadata;

    private final RestMethodMetadata feignMethodMetadata;


    FeignMethodMetadata(GenericService dubboGenericService, RestMethodMetadata dubboRestMethodMetadata,
                        RestMethodMetadata feignMethodMetadata) {
        this.dubboGenericService = dubboGenericService;
        this.dubboRestMethodMetadata = dubboRestMethodMetadata;
        this.feignMethodMetadata = feignMethodMetadata;
    }

    GenericService getDubboGenericService() {
        return dubboGenericService;
    }

    RestMethodMetadata getDubboRestMethodMetadata() {
        return dubboRestMethodMetadata;
    }

    RestMethodMetadata getFeignMethodMetadata() {
        return feignMethodMetadata;
    }
}
