
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.springframework.http.HttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import static org.springframework.cloud.alibaba.dubbo.http.util.HttpUtils.getParameters;

/**
 * Parses and matches a single param expression to a request.
 * <p>
 * The some source code is scratched from org.springframework.web.servlet.mvc.condition.ParamsRequestCondition.ParamExpression
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
class ParamExpression extends AbstractNameValueExpression<String> {

    ParamExpression(String expression) {
        super(expression);
    }

    @Override
    protected boolean isCaseSensitiveName() {
        return true;
    }

    @Override
    protected String parseValue(String valueExpression) {
        return valueExpression;
    }

    @Override
    protected boolean matchName(HttpRequest request) {
        MultiValueMap<String, String> parametersMap = getParameters(request);
        return parametersMap.containsKey(this.name);
    }

    @Override
    protected boolean matchValue(HttpRequest request) {
        MultiValueMap<String, String> parametersMap = getParameters(request);
        String parameterValue = parametersMap.getFirst(this.name);
        return ObjectUtils.nullSafeEquals(this.value, parameterValue);
    }
}
