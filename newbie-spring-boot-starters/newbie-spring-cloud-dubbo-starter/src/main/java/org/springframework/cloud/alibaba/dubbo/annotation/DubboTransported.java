/*
 * 版权所有 (c) 2019-2029, halower (halower@foxmail.com).
 *
 * Apache 2.0 License 同时该协议为补充协议，不允许 996 工作制度企业使用该开源软件
 *
 * 反996许可证版本1.0
 *
 * 在符合下列条件的情况下，特此免费向任何得到本授权作品的副本（包括源代码、文件和/或相关内容，以下
 * 统称为“授权作品”）的个人和法人实体授权：被授权个人或法人实体有权以任何目的处置授权作品，包括但
 * 不限于使用、复制，修改，衍生利用、散布，发布和再许可：
 *
 * 1. 个人或法人实体必须在许可作品的每个再散布或衍生副本上包含以上版权声明和本许可证，不得自行修
 * 改。
 * 2. 个人或法人实体必须严格遵守与个人实际所在地或个人出生地或归化地、或法人实体注册地或经营地
 * （以较严格者为准）的司法管辖区所有适用的与劳动和就业相关法律、法规、规则和标准。如果该司法管辖
 * 区没有此类法律、法规、规章和标准或其法律、法规、规章和标准不可执行，则个人或法人实体必须遵守国
 * 际劳工标准的核心公约。
 * 3. 个人或法人不得以任何方式诱导、暗示或强迫其全职或兼职员工或其独立承包人以口头或书面形式同意直接或
 * 间接限制、削弱或放弃其所拥有的，受相关与劳动和就业有关的法律、法规、规则和标准保护的权利或补救
 * 措施，无论该等书面或口头协议是否被该司法管辖区的法律所承认，该等个人或法人实体也不得以任何方法
 * 限制其雇员或独立承包人向版权持有人或监督许可证合规情况的有关当局报告或投诉上述违反许可证的行为
 * 的权利。
 *
 * 该授权作品是"按原样"提供，不做任何明示或暗示的保证，包括但不限于对适销性、特定用途适用性和非侵
 * 权性的保证。在任何情况下，无论是在合同诉讼、侵权诉讼或其他诉讼中，版权持有人均不承担因本软件或
 * 本软件的使用或其他交易而产生、引起或与之相关的任何索赔、损害或其他责任。
 */
package org.springframework.cloud.alibaba.dubbo.annotation;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.ExporterListener;
import org.apache.dubbo.rpc.Filter;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
     * Customized parameter key-value pair, for example: {key1, value1, key2, value2}
     *
     * @see Reference#parameters()
     */
    String[] parameters() default {};
}
