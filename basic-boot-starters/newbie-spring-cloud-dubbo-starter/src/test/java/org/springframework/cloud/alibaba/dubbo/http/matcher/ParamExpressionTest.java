
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpRequest;

import static org.springframework.cloud.alibaba.dubbo.http.DefaultHttpRequest.builder;

/**
 * {@link ParamExpression} Test
 *
 *
 */
public class ParamExpressionTest extends AbstractNameValueExpressionTest<ParamExpression> {

    @Test
    public void testIsCaseSensitiveName() {
        Assert.assertTrue(createExpression("a=1").isCaseSensitiveName());
        Assert.assertTrue(createExpression("a=!1").isCaseSensitiveName());
        Assert.assertTrue(createExpression("b=1").isCaseSensitiveName());
    }

    @Test
    public void testMatch() {

        ParamExpression expression = createExpression("a=1");
        HttpRequest request = builder().build();

        Assert.assertFalse(expression.match(request));

        request = builder().param("a", "").build();
        Assert.assertFalse(expression.match(request));

        request = builder().param("a", "2").build();
        Assert.assertFalse(expression.match(request));

        request = builder().param("", "1").build();
        Assert.assertFalse(expression.match(request));

        request = builder().param("a", "1").build();
        Assert.assertTrue(expression.match(request));
    }

}
