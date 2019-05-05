
package org.springframework.cloud.alibaba.dubbo.annotation;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.ExporterListener;
import org.apache.dubbo.rpc.Filter;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.apache.dubbo.common.Constants.DEFAULT_RETRIES;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface DubboTransported {

    /**
     * The protocol of Dubbo transport whose value could be used the placeholder "dubbo.transport.protocol"
     *
     * @return the default protocol is "dubbo"
     */
    String protocol() default "${dubbo.transport.protocol:dubbo}";

    /**
     * The cluster of Dubbo transport whose value could be used the placeholder "dubbo.transport.cluster"
     *
     * @return the default cluster is "failover"
     */
    String cluster() default "${dubbo.transport.cluster:failover}";

    /**
     * Whether to reconnect if connection is lost, if not specify, reconnect is enabled by default, and the interval
     * for retry connecting is 2000 ms
     *
     * @see Constants#DEFAULT_RECONNECT_PERIOD
     * @see Reference#reconnect()
     */
    String reconnect() default "${dubbo.transport.reconnect:2000}";

    /**
     * Maximum connections service provider can accept, default value is 0 - connection is shared
     *
     * @see Reference#connections()
     */
    int connections() default 0;

    /**
     * Service invocation retry times
     *
     * @see Constants#DEFAULT_RETRIES
     * @see Reference#retries()
     */
    int retries() default DEFAULT_RETRIES;

    /**
     * Load balance strategy, legal values include: random, roundrobin, leastactive
     *
     * @see Constants#DEFAULT_LOADBALANCE
     * @see Reference#loadbalance()
     */
    String loadbalance() default "${dubbo.transport.loadbalance:}";

    /**
     * Maximum active requests allowed, default value is 0
     *
     * @see Reference#actives()
     */
    int actives() default 0;

    /**
     * Timeout value for service invocation, default value is 0
     *
     * @see Reference#timeout()
     */
    int timeout() default 0;

    /**
     * Specify cache implementation for service invocation, legal values include: lru, threadlocal, jcache
     *
     * @see Reference#cache()
     */
    String cache() default "${dubbo.transport.cache:}";

    /**
     * Filters for service invocation
     *
     * @see Filter
     * @see Reference#filter()
     */
    String[] filter() default {};

    /**
     * Listeners for service exporting and unexporting
     *
     * @see ExporterListener
     * @see Reference#listener()
     */
    String[] listener() default {};

    /**
     * Customized parameter name-value pair, for example: {key1, value1, key2, value2}
     *
     * @see Reference#parameters()
     */
    String[] parameters() default {};
}
