
package org.springframework.cloud.alibaba.dubbo.client.loadbalancer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.IOException;

/**
 * Dubbo {@link HttpOutputMessage} implementation
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
class DubboHttpOutputMessage implements HttpOutputMessage {

    private final FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();

    private final HttpHeaders httpHeaders = new HttpHeaders();

    @Override
    public FastByteArrayOutputStream getBody() throws IOException {
        return outputStream;
    }

    @Override
    public HttpHeaders getHeaders() {
        return httpHeaders;
    }
}
