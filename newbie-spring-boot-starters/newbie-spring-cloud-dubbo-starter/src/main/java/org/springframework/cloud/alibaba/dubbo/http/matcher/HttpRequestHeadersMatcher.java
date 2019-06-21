
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * {@link HttpRequest} headers {@link HttpRequestMatcher matcher}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class HttpRequestHeadersMatcher extends AbstractHttpRequestMatcher {

    private final Set<HeaderExpression> expressions;

    public HttpRequestHeadersMatcher(String... headers) {
        this.expressions = parseExpressions(headers);
    }

    private static Set<HeaderExpression> parseExpressions(String... headers) {
        Set<HeaderExpression> expressions = new LinkedHashSet<>();
        for (String header : headers) {
            HeaderExpression expr = new HeaderExpression(header);
            if (HttpHeaders.ACCEPT.equalsIgnoreCase(expr.name) ||
                    HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(expr.name)) {
                continue;
            }
            expressions.add(expr);
        }
        return expressions;
    }

    @Override
    public boolean match(HttpRequest request) {
        for (HeaderExpression expression : this.expressions) {
            if (!expression.match(request)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected Collection<HeaderExpression> getContent() {
        return this.expressions;
    }

    @Override
    protected String getToStringInfix() {
        return " && ";
    }
}
