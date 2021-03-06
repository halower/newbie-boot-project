/*
 * Apache License
 *
 * Copyright (c) 2019  halower (halower@foxmail.com).
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.springframework.cloud.alibaba.dubbo.annotation;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.ExporterListener;
import org.apache.dubbo.rpc.Filter;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.*;

import static org.apache.dubbo.common.Constants.DEFAULT_RETRIES;

/**
 * {@link DubboTransported @DubboTransported} annotation indicates that the traditional Spring Cloud Service-to-Service call is transported
 * by Dubbo under the hood, there are two main scenarios:
 * <ol>
 * <li>{@link FeignClient @FeignClient} annotated classes:
 * <ul>
 * If {@link DubboTransported @DubboTransported} annotated classes, the invocation of all methods of
 * {@link FeignClient @FeignClient} annotated classes.
 * </ul>
 * <ul>
 * If {@link DubboTransported @DubboTransported} annotated methods of {@link FeignClient @FeignClient} annotated classes.
 * </ul>
 * </li>
 * <li>{@link LoadBalanced @LoadBalanced} {@link RestTemplate} annotated field, method and parameters</li>
 * </ol>
 * <p>
 *
 * @see FeignClient
 * @see LoadBalanced
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface DubboTransported {

    /**
     * The protocol of Dubbo transport whose value could be used the placeholder "services.transport.protocol"
     *
     * @return the default protocol is "services"
     */
    String protocol() default "${services.transport.protocol:services}";

    /**
     * The cluster of Dubbo transport whose value could be used the placeholder "services.transport.cluster"
     *
     * @return the default cluster is "failover"
     */
    String cluster() default "${services.transport.cluster:failover}";

    /**
     * Whether to reconnect if connection is lost, if not specify, reconnect is enabled by default, and the interval
     * for retry connecting is 2000 ms
     *
     * @see Constants#DEFAULT_RECONNECT_PERIOD
     * @see Reference#reconnect()
     */
    String reconnect() default "${services.transport.reconnect:2000}";

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
    String loadbalance() default "${services.transport.loadbalance:}";

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
    String cache() default "${services.transport.cache:}";

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
     * Customized parameter key-value pair, for example: {key1, value1, key2, value2}
     *
     * @see Reference#parameters()
     */
    String[] parameters() default {};
}
