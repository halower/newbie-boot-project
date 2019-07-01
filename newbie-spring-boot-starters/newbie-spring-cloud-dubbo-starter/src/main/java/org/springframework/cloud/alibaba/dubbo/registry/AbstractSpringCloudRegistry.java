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
package org.springframework.cloud.alibaba.dubbo.registry;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.RegistryFactory;
import org.apache.dubbo.registry.support.FailbackRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.alibaba.dubbo.metadata.repository.DubboServiceMetadataRepository;
import org.springframework.cloud.alibaba.dubbo.service.DubboMetadataService;
import org.springframework.cloud.alibaba.dubbo.service.DubboMetadataServiceProxy;
import org.springframework.cloud.alibaba.dubbo.util.JSONUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.dubbo.common.Constants.APPLICATION_KEY;
import static org.apache.dubbo.common.Constants.GROUP_KEY;
import static org.apache.dubbo.common.Constants.PROTOCOL_KEY;
import static org.apache.dubbo.common.Constants.PROVIDER_SIDE;
import static org.apache.dubbo.common.Constants.SIDE_KEY;
import static org.apache.dubbo.common.Constants.VERSION_KEY;
import static org.springframework.util.StringUtils.hasText;

/**
 * Abstract Dubbo {@link RegistryFactory} uses Spring Cloud Service Registration abstraction, whose protocol is "spring-cloud"
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public abstract class AbstractSpringCloudRegistry extends FailbackRegistry {

    /**
     * The parameter name of {@link #servicesLookupInterval}
     */
    public static final String SERVICES_LOOKUP_INTERVAL_PARAM_NAME = "dubbo.services.lookup.interval";

    protected static final String DUBBO_METADATA_SERVICE_CLASS_NAME = DubboMetadataService.class.getName();

    private static final Set<String> SCHEDULER_TASKS = new HashSet<>();

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The interval in second of lookup service names(only for Dubbo-OPS)
     */
    private final long servicesLookupInterval;

    private final DiscoveryClient discoveryClient;

    private final DubboServiceMetadataRepository repository;

    private final DubboMetadataServiceProxy dubboMetadataConfigServiceProxy;

    private final JSONUtils jsonUtils;


    protected final ScheduledExecutorService servicesLookupScheduler;

    public AbstractSpringCloudRegistry(URL url,
                                       DiscoveryClient discoveryClient,
                                       DubboServiceMetadataRepository dubboServiceMetadataRepository,
                                       DubboMetadataServiceProxy dubboMetadataConfigServiceProxy,
                                       JSONUtils jsonUtils,
                                       ScheduledExecutorService servicesLookupScheduler) {
        super(url);
        this.servicesLookupInterval = url.getParameter(SERVICES_LOOKUP_INTERVAL_PARAM_NAME, 60L);
        this.discoveryClient = discoveryClient;
        this.repository = dubboServiceMetadataRepository;
        this.dubboMetadataConfigServiceProxy = dubboMetadataConfigServiceProxy;
        this.jsonUtils = jsonUtils;
        this.servicesLookupScheduler = servicesLookupScheduler;
    }

    protected boolean shouldRegister(URL url) {
        String side = url.getParameter(SIDE_KEY);

        boolean should = PROVIDER_SIDE.equals(side); // Only register the Provider.

        if (!should) {
            if (logger.isDebugEnabled()) {
                logger.debug("The URL[{}] should not be registered.", url.toString());
            }
        }

        return should;
    }

    @Override
    public final void doRegister(URL url) {
        if (!shouldRegister(url)) {
            return;
        }
        doRegister0(url);
    }

    /**
     * The sub-type should implement to register
     *
     * @param url {@link URL}
     */
    protected abstract void doRegister0(URL url);

    @Override
    public final void doUnregister(URL url) {
        if (!shouldRegister(url)) {
            return;
        }
        doUnregister0(url);
    }

    /**
     * The sub-type should implement to unregister
     *
     * @param url {@link URL}
     */
    protected abstract void doUnregister0(URL url);

    @Override
    public final void doSubscribe(URL url, NotifyListener listener) {

        if (isAdminURL(url)) {
            // TODO in future
        } else if (isDubboMetadataServiceURL(url)) { // for DubboMetadataService
            subscribeDubboMetadataServiceURLs(url, listener);
        } else { // for general Dubbo Services
            subscribeDubboServiceURLs(url, listener);
        }
    }

    protected void subscribeDubboServiceURLs(URL url, NotifyListener listener) {

        doSubscribeDubboServiceURLs(url, listener);

        submitSchedulerTaskIfAbsent(url, listener);
    }

    private void submitSchedulerTaskIfAbsent(URL url, NotifyListener listener) {
        String taskId = url.toIdentityString();
        if (SCHEDULER_TASKS.add(taskId)) {
            schedule(() -> doSubscribeDubboServiceURLs(url, listener));
        }
    }

    protected void doSubscribeDubboServiceURLs(URL url, NotifyListener listener) {

        Set<String> subscribedServices = repository.getSubscribedServices();

        subscribedServices.stream()
                .map(dubboMetadataConfigServiceProxy::getProxy)
                .filter(Objects::nonNull)
                .forEach(dubboMetadataService -> {
                    List<URL> exportedURLs = getExportedURLs(dubboMetadataService, url);
                    List<URL> allSubscribedURLs = new LinkedList<>();
                    for (URL exportedURL : exportedURLs) {
                        String serviceName = exportedURL.getParameter(APPLICATION_KEY);
                        List<ServiceInstance> serviceInstances = getServiceInstances(serviceName);
                        String protocol = exportedURL.getProtocol();
                        List<URL> subscribedURLs = new LinkedList<>();
                        serviceInstances.forEach(serviceInstance -> {
                            Integer port = repository.getDubboProtocolPort(serviceInstance, protocol);
                            String host = serviceInstance.getHost();
                            if (port == null) {
                                if (logger.isWarnEnabled()) {
                                    logger.warn("The protocol[{}] port of Dubbo  service instance[host : {}] " +
                                            "can't be resolved", protocol, host);
                                }
                            } else {
                                URL subscribedURL = new URL(protocol, host, port, exportedURL.getParameters());
                                subscribedURLs.add(subscribedURL);
                            }
                        });

                        if (logger.isDebugEnabled()) {
                            logger.debug("The subscribed URL[{}] will notify all URLs : {}", url, subscribedURLs);
                        }

                        allSubscribedURLs.addAll(subscribedURLs);
                    }

                    listener.notify(allSubscribedURLs);
                });
    }

    private List<ServiceInstance> getServiceInstances(String serviceName) {
        return hasText(serviceName) ? doGetServiceInstances(serviceName) : emptyList();
    }

    private List<ServiceInstance> doGetServiceInstances(String serviceName) {
        List<ServiceInstance> serviceInstances = emptyList();
        try {
            serviceInstances = discoveryClient.getInstances(serviceName);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
        }
        return serviceInstances;
    }

    private List<URL> getExportedURLs(DubboMetadataService dubboMetadataService, URL url) {
        String serviceInterface = url.getServiceInterface();
        String group = url.getParameter(GROUP_KEY);
        String version = url.getParameter(VERSION_KEY);
        // The subscribed protocol may be null
        String subscribedProtocol = url.getParameter(PROTOCOL_KEY);
        String exportedURLsJSON = dubboMetadataService.getExportedURLs(serviceInterface, group, version);
        return jsonUtils
                .toURLs(exportedURLsJSON)
                .stream()
                .filter(exportedURL ->
                        subscribedProtocol == null || subscribedProtocol.equalsIgnoreCase(exportedURL.getProtocol())
                ).collect(Collectors.toList());
    }

    private void subscribeDubboMetadataServiceURLs(URL url, NotifyListener listener) {
        String serviceInterface = url.getServiceInterface();
        String group = url.getParameter(GROUP_KEY);
        String version = url.getParameter(VERSION_KEY);
        String protocol = url.getParameter(PROTOCOL_KEY);
        List<URL> urls = repository.findSubscribedDubboMetadataServiceURLs(serviceInterface, group, version, protocol);
        listener.notify(urls);
    }

    @Override
    public final void doUnsubscribe(URL url, NotifyListener listener) {
        if (isAdminURL(url)) {
            shutdownServiceNamesLookup();
        }
    }

    @Override
    public boolean isAvailable() {
        return !discoveryClient.getServices().isEmpty();
    }

    protected void shutdownServiceNamesLookup() {
        if (servicesLookupScheduler != null) {
            servicesLookupScheduler.shutdown();
        }
    }

    protected boolean isAdminURL(URL url) {
        return Constants.ADMIN_PROTOCOL.equals(url.getProtocol());
    }

    protected boolean isDubboMetadataServiceURL(URL url) {
        return DUBBO_METADATA_SERVICE_CLASS_NAME.equals(url.getServiceInterface());
    }

    protected ScheduledFuture<?> schedule(Runnable runnable) {
        return this.servicesLookupScheduler.scheduleAtFixedRate(runnable, servicesLookupInterval,
                servicesLookupInterval, TimeUnit.SECONDS);
    }
}
