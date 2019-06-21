
package org.springframework.cloud.alibaba.dubbo.http.matcher;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.http.client.MockClientHttpRequest;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * {@link HttpRequestParamsMatcher} Test
 *
 *
 */
public class HttpRequestParamsMatcherTest {

//    @Test
//    public void testGetParams() {
//
//        HttpRequestParamsMatcher matcher = new HttpRequestParamsMatcher(
//                "a  ",
//                "a =1",
//                "b = 2",
//                "b = 3 ",
//                " c = 4 ",
//                "    d"
//        );
//
//        Map<String, List<String>> params = matcher.getParams();
//        Assert.assertEquals(4, params.size());
//        Assert.assertTrue(params.containsKey("a"));
//        Assert.assertTrue(params.containsKey("b"));
//        Assert.assertTrue(params.containsKey("c"));
//        Assert.assertTrue(params.containsKey("d"));
//        Assert.assertFalse(params.containsKey("e"));
//
//        List<String> values = params.get("a");
//        Assert.assertEquals(2, values.size());
//        Assert.assertEquals("", values.get(0));
//        Assert.assertEquals("1", values.get(1));
//
//        values = params.get("b");
//        Assert.assertEquals(2, values.size());
//        Assert.assertEquals("2", values.get(0));
//        Assert.assertEquals("3", values.get(1));
//
//        values = params.get("c");
//        Assert.assertEquals(1, values.size());
//        Assert.assertEquals("4", values.get(0));
//
//        values = params.get("d");
//        Assert.assertEquals(1, values.size());
//        Assert.assertEquals("", values.get(0));
//    }

    @Test
    public void testEquals() {

        HttpRequestParamsMatcher matcher = new HttpRequestParamsMatcher("a  ", "a = 1");

        MockClientHttpRequest request = new MockClientHttpRequest();

        request.setURI(URI.create("http://dummy/?a"));
        Assert.assertTrue(matcher.match(request));
        request.setURI(URI.create("http://dummy/?a&a=1"));
        Assert.assertTrue(matcher.match(request));

        matcher = new HttpRequestParamsMatcher("a  ", "a =1", "b", "b=2");
        request.setURI(URI.create("http://dummy/?a&a=1&b"));
        Assert.assertTrue(matcher.match(request));
        request.setURI(URI.create("http://dummy/?a&a=1&b&b=2"));
        Assert.assertTrue(matcher.match(request));

        matcher = new HttpRequestParamsMatcher("a  ", "a =1", "b", "b=2", "b = 3 ");
        request.setURI(URI.create("http://dummy/?a&a=1&b&b=2&b=3"));
        Assert.assertTrue(matcher.match(request));

        request.setURI(URI.create("http://dummy/?d=1"));
        Assert.assertFalse(matcher.match(request));
    }
}
