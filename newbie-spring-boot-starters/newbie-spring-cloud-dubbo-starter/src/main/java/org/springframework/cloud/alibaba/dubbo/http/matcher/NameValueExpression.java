
package org.springframework.cloud.alibaba.dubbo.http.matcher;


/**
 * A contract for {@code "name!=value"} style expression used to specify request
 * parameters and request header in HTTP request
 * <p>
 * The some source code is scratched from org.springframework.web.servlet.mvc.condition.NameValueExpression
 *
 * @param <T> the value type
 * @author Rossen Stoyanchev
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
interface NameValueExpression<T> {

    String getName();

    T getValue();

    boolean isNegated();

}
