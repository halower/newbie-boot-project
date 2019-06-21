
package org.springframework.cloud.alibaba.dubbo.openfeign;

import org.apache.dubbo.rpc.service.GenericService;

import org.springframework.cloud.alibaba.dubbo.metadata.RestMethodMetadata;
import org.springframework.cloud.alibaba.dubbo.service.DubboGenericServiceExecutionContext;
import org.springframework.cloud.alibaba.dubbo.service.DubboGenericServiceExecutionContextFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import static org.apache.dubbo.common.utils.PojoUtils.realize;

/**
 * Dubbo {@link GenericService} for {@link InvocationHandler}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class DubboInvocationHandler implements InvocationHandler {

    private final Map<Method, FeignMethodMetadata> feignMethodMetadataMap;

    private final InvocationHandler defaultInvocationHandler;

    private final DubboGenericServiceExecutionContextFactory contextFactory;

    private final ClassLoader classLoader;

    public DubboInvocationHandler(Map<Method, FeignMethodMetadata> feignMethodMetadataMap,
                                  InvocationHandler defaultInvocationHandler,
                                  ClassLoader classLoader,
                                  DubboGenericServiceExecutionContextFactory contextFactory) {
        this.feignMethodMetadataMap = feignMethodMetadataMap;
        this.defaultInvocationHandler = defaultInvocationHandler;
        this.classLoader = classLoader;
        this.contextFactory = contextFactory;
    }

    @Override
    public Object invoke(Object proxy, Method feignMethod, Object[] args) throws Throwable {

        FeignMethodMetadata feignMethodMetadata = feignMethodMetadataMap.get(feignMethod);

        if (feignMethodMetadata == null) {
            return defaultInvocationHandler.invoke(proxy, feignMethod, args);
        }

        GenericService dubboGenericService = feignMethodMetadata.getDubboGenericService();
        RestMethodMetadata dubboRestMethodMetadata = feignMethodMetadata.getDubboRestMethodMetadata();
        RestMethodMetadata feignRestMethodMetadata = feignMethodMetadata.getFeignMethodMetadata();

        DubboGenericServiceExecutionContext context = contextFactory.create(dubboRestMethodMetadata, feignRestMethodMetadata, args);

        String methodName = context.getMethodName();
        String[] parameterTypes = context.getParameterTypes();
        Object[] parameters = context.getParameters();

        Object result = dubboGenericService.$invoke(methodName, parameterTypes, parameters);

        Class<?> returnType = getReturnType(dubboRestMethodMetadata);

        return realize(result, returnType);
    }

    private Class<?> getReturnType(RestMethodMetadata dubboRestMethodMetadata) {
        String returnType = dubboRestMethodMetadata.getReturnType();
        return ClassUtils.resolveClassName(returnType, classLoader);
    }
}
