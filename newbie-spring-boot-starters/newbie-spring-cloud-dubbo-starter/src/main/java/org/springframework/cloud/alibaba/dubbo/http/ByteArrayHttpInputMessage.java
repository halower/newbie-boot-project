
package org.springframework.cloud.alibaba.dubbo.http;

import org.apache.dubbo.common.io.UnsafeByteArrayInputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;

/**
 * Byte array {@link HttpInputMessage} implementation
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
class ByteArrayHttpInputMessage implements HttpInputMessage {

    private final HttpHeaders httpHeaders;

    private final InputStream inputStream;

    public ByteArrayHttpInputMessage(byte[] body) {
        this(new HttpHeaders(), body);
    }

    public ByteArrayHttpInputMessage(HttpHeaders httpHeaders, byte[] body) {
        this.httpHeaders = httpHeaders;
        this.inputStream = new UnsafeByteArrayInputStream(body);
    }

    @Override
    public InputStream getBody() throws IOException {
        return inputStream;
    }

    @Override
    public HttpHeaders getHeaders() {
        return httpHeaders;
    }
}
