
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.springframework.cloud.alibaba.dubbo.metadata.RequestMetadata;

import static org.springframework.cloud.alibaba.dubbo.http.util.HttpUtils.toNameAndValues;

/**
 * {@link RequestMetadata} {@link HttpRequestMatcher} implementation
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class RequestMetadataMatcher extends CompositeHttpRequestMatcher {

    public RequestMetadataMatcher(RequestMetadata metadata) {
        super(
                // method
                new HttpRequestMethodsMatcher(metadata.getMethod()),
                // url
                new HttpRequestPathMatcher(metadata.getPath()),
                // params
                new HttpRequestParamsMatcher(toNameAndValues(metadata.getParams())),
                // headers
                new HttpRequestHeadersMatcher(toNameAndValues(metadata.getHeaders())),
                // consumes
                new HttpRequestConsumersMatcher(metadata.getConsumes().toArray(new String[0])),
                // produces
                new HttpRequestProducesMatcher(metadata.getProduces().toArray(new String[0]))
        );
    }
}
