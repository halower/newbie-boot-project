
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;

import java.lang.reflect.Constructor;

/**
 * {@link AbstractMediaTypeExpression} Test
 *
 *
 */
public abstract class AbstractMediaTypeExpressionTest<T extends AbstractMediaTypeExpression> {

    protected T createExpression(String expression) {
        ResolvableType resolvableType = ResolvableType.forType(getClass().getGenericSuperclass());
        Class<T> firstGenericType = (Class<T>) resolvableType.resolveGeneric(0);
        Constructor<T> constructor = null;
        try {
            constructor = firstGenericType.getDeclaredConstructor(String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return BeanUtils.instantiateClass(constructor, expression);
    }

    @Test
    public void testGetMediaTypeAndNegated() {
        // Normal
        AbstractMediaTypeExpression expression = createExpression(MediaType.APPLICATION_JSON_VALUE);
        Assert.assertEquals(MediaType.APPLICATION_JSON, expression.getMediaType());
        Assert.assertFalse(expression.isNegated());

        // Negated
        expression = createExpression("!" + MediaType.APPLICATION_JSON_VALUE);
        Assert.assertEquals(MediaType.APPLICATION_JSON, expression.getMediaType());
        Assert.assertTrue(expression.isNegated());
    }

    @Test
    public void testEqualsAndHashCode() {
        Assert.assertEquals(createExpression(MediaType.APPLICATION_JSON_VALUE), createExpression(MediaType.APPLICATION_JSON_VALUE));
        Assert.assertEquals(createExpression(MediaType.APPLICATION_JSON_VALUE).hashCode(),
                createExpression(MediaType.APPLICATION_JSON_VALUE).hashCode());
    }

    @Test
    public void testCompareTo() {
        Assert.assertEquals(0,
                createExpression(MediaType.APPLICATION_JSON_VALUE).compareTo(createExpression(MediaType.APPLICATION_JSON_VALUE)));
    }
}
