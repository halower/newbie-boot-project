
package org.springframework.cloud.alibaba.dubbo.actuate.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.cloud.alibaba.dubbo.service.DubboMetadataService;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Dubbo Rest Metadata {@link Endpoint}
 */
@Endpoint(id = "dubborestmetadata")
public class DubboRestMetadataEndpoint {

    @Autowired
    private DubboMetadataService dubboMetadataService;

    @ReadOperation(produces = APPLICATION_JSON_UTF8_VALUE)
    public String get() {
        return dubboMetadataService.getServiceRestMetadata();
    }
}
