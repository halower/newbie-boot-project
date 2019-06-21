
package org.springframework.cloud.alibaba.dubbo.http.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * {@link HttpMessageConverter} Holder with {@link MediaType}.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class HttpMessageConverterHolder {

    private final MediaType mediaType;

    private final HttpMessageConverter<?> converter;

    public HttpMessageConverterHolder(MediaType mediaType, HttpMessageConverter<?> converter) {
        this.mediaType = mediaType;
        this.converter = converter;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public HttpMessageConverter<?> getConverter() {
        return converter;
    }
}
