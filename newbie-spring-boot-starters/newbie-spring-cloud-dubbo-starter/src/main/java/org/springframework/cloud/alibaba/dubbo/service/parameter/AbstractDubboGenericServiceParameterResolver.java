
package org.springframework.cloud.alibaba.dubbo.service.parameter;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import static org.springframework.context.ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME;
import static org.springframework.util.ClassUtils.resolveClassName;

/**
 * Abstract {@link DubboGenericServiceParameterResolver} implementation
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public abstract class AbstractDubboGenericServiceParameterResolver implements DubboGenericServiceParameterResolver,
        BeanClassLoaderAware {

    private int order;

    @Autowired(required = false)
    @Qualifier(CONVERSION_SERVICE_BEAN_NAME)
    private ConversionService conversionService = new DefaultFormattingConversionService();

    private ClassLoader classLoader;

    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    protected Class<?> resolveClass(String className) {
        return resolveClassName(className, classLoader);
    }

    protected Object resolveValue(Object parameterValue, String parameterType) {
        Class<?> targetType = resolveClass(parameterType);
        return resolveValue(parameterValue, targetType);
    }

    protected Object resolveValue(Object parameterValue, Class<?> parameterType) {
        return conversionService.convert(parameterValue, parameterType);
    }
}
