
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.springframework.http.HttpRequest;

/**
 * {@link HttpRequest} Matcher
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public interface HttpRequestMatcher {

    /**
     * Match {@link HttpRequest} or not
     *
     * @param request The {@link HttpRequest} instance
     * @return if matched, return <code>true</code>, or <code>false</code>.
     */
    boolean match(HttpRequest request);
}
