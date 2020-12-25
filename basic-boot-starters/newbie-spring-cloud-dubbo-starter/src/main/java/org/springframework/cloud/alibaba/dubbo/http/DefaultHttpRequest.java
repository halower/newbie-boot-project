/*
 * Apache License
 *
 * Copyright (c) 2019  halower (halower@foxmail.com).
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.springframework.cloud.alibaba.dubbo.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.web.util.UriComponentsBuilder.fromPath;

/**
 * Default {@link HttpRequest} implementation
 *
 * @Author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class DefaultHttpRequest implements HttpRequest {

    private final String method;

    private final URI uri;

    private final HttpHeaders headers = new HttpHeaders();

    public DefaultHttpRequest(String method, String path, Map<String, List<String>> params,
                              Map<String, List<String>> headers) {
        this.method = method == null ? HttpMethod.GET.name() : method.toUpperCase();
        this.uri = buildURI(path, params);
        this.headers.putAll(headers);
    }

    private URI buildURI(String path, Map<String, List<String>> params) {
        UriComponentsBuilder builder = fromPath(path)
                .queryParams(new LinkedMultiValueMap<>(params));
        return builder.build().toUri();
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.resolve(getMethodValue());
    }

    @Override
    public String getMethodValue() {
        return method;
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@link HttpRequest} Builder
     */
    public static class Builder {

        String method;

        String path;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder param(String name, String value) {
            this.params.add(name, value);
            return this;
        }

        public Builder header(String name, String value) {
            this.headers.add(name, value);
            return this;
        }

        public Builder params(Map<String, List<String>> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder headers(Map<String, List<String>> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public HttpRequest build() {
            return new DefaultHttpRequest(method, path, params, headers);
        }
    }


}