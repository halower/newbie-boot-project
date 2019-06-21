
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link HttpRequest} 'Content-Type' header {@link HttpRequestMatcher matcher}
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class HttpRequestConsumersMatcher extends AbstractHttpRequestMatcher {

    private final List<ConsumeMediaTypeExpression> expressions;

    /**
     * Creates a new instance from 0 or more "consumes" expressions.
     *
     * @param consumes consumes expressions if 0 expressions are provided,
     *                 the condition will match to every request
     */
    public HttpRequestConsumersMatcher(String... consumes) {
        this(consumes, null);
    }

    /**
     * Creates a new instance with "consumes" and "header" expressions.
     * "Header" expressions where the header name is not 'Content-Type' or have
     * no header value defined are ignored. If 0 expressions are provided in
     * total, the condition will match to every request
     *
     * @param consumes consumes expressions
     * @param headers  headers expressions
     */
    public HttpRequestConsumersMatcher(String[] consumes, String[] headers) {
        this(parseExpressions(consumes, headers));
    }

    /**
     * Private constructor accepting parsed media type expressions.
     */
    private HttpRequestConsumersMatcher(Collection<ConsumeMediaTypeExpression> expressions) {
        this.expressions = new ArrayList<>(expressions);
        Collections.sort(this.expressions);
    }

    @Override
    public boolean match(HttpRequest request) {

        if (expressions.isEmpty()) {
            return true;
        }

        HttpHeaders httpHeaders = request.getHeaders();

        MediaType contentType = httpHeaders.getContentType();

        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        }

        for (ConsumeMediaTypeExpression expression : expressions) {
            if (!expression.match(contentType)) {
                return false;
            }
        }

        return true;
    }

    private static Set<ConsumeMediaTypeExpression> parseExpressions(String[] consumes, String[] headers) {
        Set<ConsumeMediaTypeExpression> result = new LinkedHashSet<>();
        if (headers != null) {
            for (String header : headers) {
                HeaderExpression expr = new HeaderExpression(header);
                if ("Content-Type".equalsIgnoreCase(expr.name) && expr.value != null) {
                    for (MediaType mediaType : MediaType.parseMediaTypes(expr.value)) {
                        result.add(new ConsumeMediaTypeExpression(mediaType, expr.negated));
                    }
                }
            }
        }
        for (String consume : consumes) {
            result.add(new ConsumeMediaTypeExpression(consume));
        }
        return result;
    }

    @Override
    protected Collection<ConsumeMediaTypeExpression> getContent() {
        return this.expressions;
    }

    @Override
    protected String getToStringInfix() {
        return " || ";
    }
}
