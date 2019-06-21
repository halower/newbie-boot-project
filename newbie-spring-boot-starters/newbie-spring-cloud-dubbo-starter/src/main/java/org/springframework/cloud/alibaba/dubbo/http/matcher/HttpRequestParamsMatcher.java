
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.springframework.http.HttpRequest;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * {@link HttpRequest} parameters {@link HttpRequestMatcher matcher}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class HttpRequestParamsMatcher extends AbstractHttpRequestMatcher {

    private final Set<ParamExpression> expressions;

    /**
     * @param params The pattern of params :
     *               <ul>
     *               <li>name=value</li>
     *               <li>name</li>
     *               </ul>
     */
    public HttpRequestParamsMatcher(String... params) {
        this.expressions = parseExpressions(params);
    }

    @Override
    public boolean match(HttpRequest request) {
        if (CollectionUtils.isEmpty(expressions)) {
            return true;
        }
        for (ParamExpression paramExpression : expressions) {
            if (paramExpression.match(request)) {
                return true;
            }
        }
        return false;
    }

    private static Set<ParamExpression> parseExpressions(String... params) {
        Set<ParamExpression> expressions = new LinkedHashSet<>();
        for (String param : params) {
            expressions.add(new ParamExpression(param));
        }
        return expressions;
    }

    @Override
    protected Collection<ParamExpression> getContent() {
        return this.expressions;
    }

    @Override
    protected String getToStringInfix() {
        return " && ";
    }
}
