
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Constructor;

/**
 * {@link AbstractNameValueExpression} Test
 *
 *
 */
public abstract class AbstractNameValueExpressionTest<T extends AbstractNameValueExpression> {

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
    public void testGetNameAndValue() {
        // Normal Name and value
        AbstractNameValueExpression expression = createExpression("a=1");
        Assert.assertEquals("a", expression.getName());
        Assert.assertFalse(expression.isNegated());

        expression = createExpression("a=1");
        Assert.assertEquals("a", expression.getName());
        Assert.assertEquals("1", expression.getValue());
        Assert.assertFalse(expression.isNegated());

        // Negated Name
        expression = createExpression("!a");
        Assert.assertEquals("a", expression.getName());
        Assert.assertTrue(expression.isNegated());

        expression = createExpression("a!=1");
        Assert.assertEquals("a", expression.getName());
        Assert.assertEquals("1", expression.getValue());
        Assert.assertTrue(expression.isNegated());
    }

    @Test
    public void testEqualsAndHashCode() {
        Assert.assertEquals(createExpression("a"), createExpression("a"));
        Assert.assertEquals(createExpression("a").hashCode(), createExpression("a").hashCode());
        Assert.assertEquals(createExpression("a=1"), createExpression("a = 1 "));
        Assert.assertEquals(createExpression("a=1").hashCode(), createExpression("a = 1 ").hashCode());
        Assert.assertNotEquals(createExpression("a"), createExpression("b"));
        Assert.assertNotEquals(createExpression("a").hashCode(), createExpression("b").hashCode());
    }
}
