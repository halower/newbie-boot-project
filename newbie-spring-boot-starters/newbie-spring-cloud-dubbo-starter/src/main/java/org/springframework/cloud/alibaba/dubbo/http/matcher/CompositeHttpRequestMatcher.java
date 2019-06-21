
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.springframework.http.HttpRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Composite {@link HttpRequestMatcher} implementation
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public abstract class CompositeHttpRequestMatcher extends AbstractHttpRequestMatcher {

    private final List<HttpRequestMatcher> matchers = new LinkedList<>();

    public CompositeHttpRequestMatcher(HttpRequestMatcher... matchers) {
        this.matchers.addAll(Arrays.asList(matchers));
    }

    public CompositeHttpRequestMatcher and(HttpRequestMatcher matcher) {
        this.matchers.add(matcher);
        return this;
    }

    @Override
    public boolean match(HttpRequest request) {
        for (HttpRequestMatcher matcher : matchers) {
            if (!matcher.match(request)) {
                return false;
            }
        }
        return true;
    }

    protected List<HttpRequestMatcher> getMatchers() {
        return this.matchers;
    }

    @Override
    protected Collection<?> getContent() {
        List<Object> content = new LinkedList<>();
        for (HttpRequestMatcher matcher : getMatchers()) {
            if (matcher instanceof AbstractHttpRequestMatcher) {
                content.addAll(((AbstractHttpRequestMatcher) matcher).getContent());
            }
        }
        return content;
    }

    @Override
    protected String getToStringInfix() {
        return " && ";
    }
}
