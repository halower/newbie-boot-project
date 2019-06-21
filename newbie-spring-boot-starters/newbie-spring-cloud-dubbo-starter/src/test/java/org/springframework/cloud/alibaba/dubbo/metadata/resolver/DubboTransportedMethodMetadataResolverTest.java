
package org.springframework.cloud.alibaba.dubbo.metadata.resolver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.alibaba.dubbo.annotation.DubboTransported;
import org.springframework.cloud.alibaba.dubbo.metadata.DubboTransportedMethodMetadata;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.mock.env.MockEnvironment;

import java.util.Set;

/**
 * {@link DubboTransportedMethodMetadataResolver} Test
 *
 *
 */
public class DubboTransportedMethodMetadataResolverTest {

    private DubboTransportedMethodMetadataResolver resolver;

    private MockEnvironment environment;

    @Before
    public void init() {
        environment = new MockEnvironment();
        resolver = new DubboTransportedMethodMetadataResolver(environment, new SpringMvcContract());
    }

    @Test
    public void testResolve() {
        Set<DubboTransportedMethodMetadata> metadataSet = resolver.resolveDubboTransportedMethodMetadataSet(TestDefaultService.class);
        Assert.assertEquals(1, metadataSet.size());
    }


    @DubboTransported
    interface TestDefaultService {

        String test(String message);

    }
}
