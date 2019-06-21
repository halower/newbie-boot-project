
package org.springframework.cloud.alibaba.dubbo.http;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpRequest;
import org.springframework.util.MultiValueMap;

/**
 * HTTP Server Request
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public interface HttpServerRequest extends HttpRequest, HttpInputMessage {

    /**
     * Return a path of current HTTP request
     *
     * @return
     */
    String getPath();

    /**
     * Return a map with parsed and decoded query parameter values.
     */
    MultiValueMap<String, String> getQueryParams();

}
