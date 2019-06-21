
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;

/**
 * {@link ConsumeMediaTypeExpression} Test
 *
 *
 */
public class ConsumeMediaTypeExpressionTest extends AbstractMediaTypeExpressionTest<ConsumeMediaTypeExpression> {

    @Test
    public void testMatch() {
        ConsumeMediaTypeExpression expression = createExpression(MediaType.ALL_VALUE);

        Assert.assertTrue(expression.match(MediaType.APPLICATION_JSON_UTF8));

        expression = createExpression(MediaType.APPLICATION_JSON_VALUE);
        Assert.assertTrue(expression.match(MediaType.APPLICATION_JSON_UTF8));

        expression = createExpression(MediaType.APPLICATION_JSON_VALUE + ";q=0.7");
        Assert.assertTrue(expression.match(MediaType.APPLICATION_JSON_UTF8));

        expression = createExpression(MediaType.TEXT_HTML_VALUE);
        Assert.assertFalse(expression.match(MediaType.APPLICATION_JSON_UTF8));
    }
}
