
package org.springframework.cloud.alibaba.dubbo.env;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.util.StringUtils.commaDelimitedListToStringArray;
import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.trimAllWhitespace;

/**
 * Dubbo Cloud {@link ConfigurationProperties Properties}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
@ConfigurationProperties(prefix = "dubbo.cloud")
public class DubboCloudProperties {

    /**
     * All services of Dubbo
     */
    public static final String ALL_DUBBO_SERVICES = "*";

    /**
     * The subscribed services, the default value is "*". The multiple value will use comma(",") as the separator.
     *
     * @see #ALL_DUBBO_SERVICES
     */
    private String subscribedServices = ALL_DUBBO_SERVICES;

    public String getSubscribedServices() {
        return subscribedServices;
    }

    public void setSubscribedServices(String subscribedServices) {
        this.subscribedServices = subscribedServices;
    }

    /**
     * Get the subscribed services as a {@link Set} with configuration order.
     *
     * @return non-null Read-only {@link Set}
     */
    public Set<String> subscribedServices() {

        String[] services = commaDelimitedListToStringArray(getSubscribedServices());

        if (services.length < 1) {
            return Collections.emptySet();
        }

        Set<String> subscribedServices = new LinkedHashSet<>();

        for (String service : services) {
            if (hasText(service)) {  // filter blank service name
                // remove all whitespace
                subscribedServices.add(trimAllWhitespace(service));
            }
        }

        return Collections.unmodifiableSet(subscribedServices);
    }
}
