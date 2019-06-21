
package org.springframework.cloud.alibaba.dubbo.service.parameter;

import org.springframework.cloud.alibaba.dubbo.http.HttpServerRequest;
import org.springframework.util.MultiValueMap;

/**
 * HTTP Request Header {@link DubboGenericServiceParameterResolver Dubbo GenericService Parameter Resolver}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class RequestHeaderServiceParameterResolver extends AbstractNamedValueServiceParameterResolver {

    public static final int DEFAULT_ORDER = 9;

    public RequestHeaderServiceParameterResolver() {
        super();
        setOrder(DEFAULT_ORDER);
    }

    @Override
    protected MultiValueMap<String, String> getNameAndValuesMap(HttpServerRequest request) {
        return request.getHeaders();
    }
}
