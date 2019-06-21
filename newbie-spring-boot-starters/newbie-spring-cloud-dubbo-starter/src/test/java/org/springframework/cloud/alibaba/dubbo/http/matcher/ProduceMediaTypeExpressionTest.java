
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;

/**
 * {@link ProduceMediaTypeExpression} Test
 *
 *
 */
public class ProduceMediaTypeExpressionTest extends AbstractMediaTypeExpressionTest<ProduceMediaTypeExpression> {

    @Test
    public void testMatch() {
        ProduceMediaTypeExpression expression = createExpression(MediaType.APPLICATION_JSON_VALUE);
        Assert.assertTrue(expression.match(Arrays.asList(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON)));

        expression = createExpression(MediaType.APPLICATION_JSON_VALUE);
        Assert.assertFalse(expression.match(Arrays.asList(MediaType.APPLICATION_XML)));
    }
}
