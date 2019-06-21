
package org.springframework.cloud.alibaba.dubbo.http.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link HttpUtils} Test
 *
 *
 */
public class HttpUtilsTest {

    @Test
    public void testEncodeAndDecode() {

        String whitespace = " ";

        String encodedValue = HttpUtils.encode(" ");

        String decodedValue = HttpUtils.decode(encodedValue);

        Assert.assertEquals(whitespace, decodedValue);
    }

}
