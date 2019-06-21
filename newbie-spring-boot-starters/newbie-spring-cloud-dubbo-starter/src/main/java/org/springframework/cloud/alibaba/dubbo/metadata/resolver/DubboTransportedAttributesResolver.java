
package org.springframework.cloud.alibaba.dubbo.metadata.resolver;

import org.springframework.cloud.alibaba.dubbo.annotation.DubboTransported;
import org.springframework.core.env.PropertyResolver;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.core.annotation.AnnotationUtils.getAnnotationAttributes;

/**
 * {@link DubboTransported} annotation attributes resolver
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class DubboTransportedAttributesResolver {

    private final PropertyResolver propertyResolver;

    public DubboTransportedAttributesResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    public Map<String, Object> resolve(DubboTransported dubboTransported) {
        Map<String, Object> attributes = getAnnotationAttributes(dubboTransported);
        return resolve(attributes);
    }

    public Map<String, Object> resolve(Map<String, Object> attributes) {
        Map<String, Object> resolvedAttributes = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                value = propertyResolver.resolvePlaceholders(value.toString());
            }
            resolvedAttributes.put(entry.getKey(), value);
        }
        return resolvedAttributes;
    }
}
