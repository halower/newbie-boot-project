package com.newbie.spi;

import com.newbie.core.exception.BusinessException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SpiFactoryBean<T> implements FactoryBean<T> {
    private Class<? extends SwaggerManager> spiClz;

    private List<SwaggerManager> list;

    public SpiFactoryBean(ApplicationContext applicationContext, Class<? extends SwaggerManager> clz) {
        this.spiClz = clz;

        Map<String, ? extends SwaggerManager> map = applicationContext.getBeansOfType(spiClz);
        list = new ArrayList<>(map.values());
        list.sort(Comparator.comparingInt(SwaggerManager::order));
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject()  {
        InvocationHandler invocationHandler = (proxy, method, args) -> {
            for (SwaggerManager spi : list) {
                if (spi.show(args[0], args[1], args[2])) {
                    return method.invoke(spi, args);
                }
            }
            throw new BusinessException("没有可执行的服务");
        };

        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{spiClz},
                invocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return spiClz;
    }
}
