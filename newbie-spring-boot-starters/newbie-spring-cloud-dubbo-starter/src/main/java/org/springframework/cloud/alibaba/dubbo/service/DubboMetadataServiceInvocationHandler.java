
package org.springframework.cloud.alibaba.dubbo.service;

import org.apache.dubbo.rpc.service.GenericService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * {@link DubboMetadataService} {@link InvocationHandler}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
class DubboMetadataServiceInvocationHandler implements InvocationHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final GenericService genericService;

    public DubboMetadataServiceInvocationHandler(String serviceName, String version, DubboGenericServiceFactory dubboGenericServiceFactory) {
        this.genericService = dubboGenericServiceFactory.create(serviceName, DubboMetadataService.class, version);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object returnValue = null;
        try {
            returnValue = genericService.$invoke(method.getName(), getParameterTypes(method), args);
        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
        return returnValue;
    }

    private String[] getParameterTypes(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return Stream.of(parameterTypes).map(Class::getName).toArray(length -> new String[length]);
    }
}
