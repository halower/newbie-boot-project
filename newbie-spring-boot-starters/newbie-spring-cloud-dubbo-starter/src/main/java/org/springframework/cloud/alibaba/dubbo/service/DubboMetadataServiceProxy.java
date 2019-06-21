
package org.springframework.cloud.alibaba.dubbo.service;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.DisposableBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * The proxy of {@link DubboMetadataService}
 */
public class DubboMetadataServiceProxy implements BeanClassLoaderAware, DisposableBean {

    private final DubboGenericServiceFactory dubboGenericServiceFactory;

    private ClassLoader classLoader;

    private final Map<String, DubboMetadataService> dubboMetadataServiceCache = new ConcurrentHashMap<>();

    public DubboMetadataServiceProxy(DubboGenericServiceFactory dubboGenericServiceFactory) {
        this.dubboGenericServiceFactory = dubboGenericServiceFactory;
    }

    /**
     * Initializes {@link DubboMetadataService}'s Proxy
     *
     * @param serviceName the service name
     * @param version     the service version
     * @return a {@link DubboMetadataService} proxy
     */
    public DubboMetadataService initProxy(String serviceName, String version) {
        return dubboMetadataServiceCache.computeIfAbsent(serviceName, name -> newProxy(name, version));
    }

    /**
     * Get a proxy instance of {@link DubboMetadataService} via the specified service name
     *
     * @param serviceName the service name
     * @return a {@link DubboMetadataService} proxy
     */
    public DubboMetadataService getProxy(String serviceName) {
        return dubboMetadataServiceCache.get(serviceName);
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void destroy() throws Exception {
        dubboMetadataServiceCache.clear();
    }

    /**
     * New a proxy instance of {@link DubboMetadataService} via the specified service name
     *
     * @param serviceName the service name
     * @param version     the service version
     * @return a {@link DubboMetadataService} proxy
     */
    protected DubboMetadataService newProxy(String serviceName, String version) {
        return (DubboMetadataService) newProxyInstance(classLoader, new Class[]{DubboMetadataService.class},
                new DubboMetadataServiceInvocationHandler(serviceName, version, dubboGenericServiceFactory));
    }
}
