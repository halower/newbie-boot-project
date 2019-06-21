
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import java.util.Collection;
import java.util.Iterator;

/**
 * Abstract {@link HttpRequestMatcher} implementation
 *
 * @author Rossen Stoyanchev
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public abstract class AbstractHttpRequestMatcher implements HttpRequestMatcher {

    /**
     * Return the discrete items a request condition is composed of.
     * <p>For example URL patterns, HTTP request methods, param expressions, etc.
     *
     * @return a collection of objects, never {@code null}
     */
    protected abstract Collection<?> getContent();

    /**
     * The notation to use when printing discrete items of content.
     * <p>For example {@code " || "} for URL patterns or {@code " && "}
     * for param expressions.
     */
    protected abstract String getToStringInfix();

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return getContent().equals(((AbstractHttpRequestMatcher) other).getContent());
    }

    @Override
    public int hashCode() {
        return getContent().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        for (Iterator<?> iterator = getContent().iterator(); iterator.hasNext(); ) {
            Object expression = iterator.next();
            builder.append(expression.toString());
            if (iterator.hasNext()) {
                builder.append(getToStringInfix());
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
