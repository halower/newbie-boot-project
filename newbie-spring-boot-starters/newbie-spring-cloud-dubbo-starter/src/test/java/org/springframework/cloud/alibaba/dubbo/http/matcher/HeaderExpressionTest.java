
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpRequest;

import static org.springframework.cloud.alibaba.dubbo.http.DefaultHttpRequest.builder;

/**
 * {@link HeaderExpression} Test
 *
 *
 */
public class HeaderExpressionTest extends AbstractNameValueExpressionTest<HeaderExpression> {

    @Test
    public void testIsCaseSensitiveName() {
        Assert.assertFalse(createExpression("a=1").isCaseSensitiveName());
        Assert.assertFalse(createExpression("a=!1").isCaseSensitiveName());
        Assert.assertFalse(createExpression("b=1").isCaseSensitiveName());
    }

    @Test
    public void testMatch() {

        HeaderExpression expression = createExpression("a=1");
        HttpRequest request = builder().build();

        Assert.assertFalse(expression.match(request));

        request = builder().header("a", "").build();
        Assert.assertFalse(expression.match(request));

        request = builder().header("a", "2").build();
        Assert.assertFalse(expression.match(request));

        request = builder().header("", "1").build();
        Assert.assertFalse(expression.match(request));

        request = builder().header("a", "1").build();
        Assert.assertTrue(expression.match(request));
    }

}
