
package org.springframework.cloud.alibaba.dubbo.service;

import org.apache.dubbo.rpc.service.GenericService;

/**
 * Dubbo {@link GenericService} execution context
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class DubboGenericServiceExecutionContext {

    private final String methodName;

    private final String[] parameterTypes;

    private final Object[] parameters;

    public DubboGenericServiceExecutionContext(String methodName, String[] parameterTypes, Object[] parameters) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }
}
