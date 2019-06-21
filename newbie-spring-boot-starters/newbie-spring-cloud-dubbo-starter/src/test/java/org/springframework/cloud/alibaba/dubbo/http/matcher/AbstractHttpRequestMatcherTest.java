
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.junit.Test;

/**
 * {@link AbstractHttpRequestMatcher} Test
 *
 *
 */
public abstract class AbstractHttpRequestMatcherTest<T extends AbstractHttpRequestMatcher> {

    @Test
    public abstract void testEqualsAndHashCode();

    @Test
    public abstract void testGetContent();

    @Test
    public abstract void testGetToStringInfix();

}
