
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.junit.Assert;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.HashSet;

/**
 * {@link HttpRequestMethodsMatcher} Test
 *
 *
 */
public class HttpRequestMethodsMatcherTest extends AbstractHttpRequestMatcherTest<HttpRequestMethodsMatcher> {

    HttpRequestMethodsMatcher matcher = new HttpRequestMethodsMatcher("GET");

    @Override
    public void testEqualsAndHashCode() {
        Assert.assertEquals(new HashSet<>(Arrays.asList(HttpMethod.GET)), matcher.getMethods());
    }

    @Override
    public void testGetContent() {
        Assert.assertEquals(new HashSet<>(Arrays.asList(HttpMethod.GET)), matcher.getContent());
    }

    @Override
    public void testGetToStringInfix() {
        Assert.assertEquals(" || ", matcher.getToStringInfix());
    }

}
