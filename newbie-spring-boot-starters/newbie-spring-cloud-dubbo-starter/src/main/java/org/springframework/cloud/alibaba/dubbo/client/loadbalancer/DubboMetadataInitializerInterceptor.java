
package org.springframework.cloud.alibaba.dubbo.client.loadbalancer;

import org.springframework.cloud.alibaba.dubbo.metadata.repository.DubboServiceMetadataRepository;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URI;

/**
 * Dubbo Metadata {@link ClientHttpRequestInterceptor} Initializing Interceptor executes intercept before
 * {@link DubboTransporterInterceptor}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class DubboMetadataInitializerInterceptor implements ClientHttpRequestInterceptor {

    private final DubboServiceMetadataRepository repository;

    public DubboMetadataInitializerInterceptor(DubboServiceMetadataRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        URI originalUri = request.getURI();

        String serviceName = originalUri.getHost();

        repository.initialize(serviceName);

        // Execute next
        return execution.execute(request, body);
    }
}
