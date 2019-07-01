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
package org.springframework.cloud.alibaba.dubbo.metadata.resolver;

import feign.Contract;
import feign.Feign;
import feign.MethodMetadata;
import feign.Util;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.cloud.alibaba.dubbo.metadata.RestMethodMetadata;
import org.springframework.cloud.alibaba.dubbo.metadata.ServiceRestMetadata;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The config resolver for {@link Feign} for {@link ServiceBean Dubbo Service Bean} in the provider side.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class DubboServiceBeanMetadataResolver implements BeanClassLoaderAware, SmartInitializingSingleton,
        MetadataResolver {

    private static final String[] CONTRACT_CLASS_NAMES = {
            "feign.jaxrs2.JAXRS2Contract",
            "org.springframework.cloud.openfeign.support.SpringMvcContract",
    };

    private final ObjectProvider<Contract> contractObjectProvider;

    private ClassLoader classLoader;

    /**
     * Feign Contracts
     */
    private Collection<Contract> contracts;

    public DubboServiceBeanMetadataResolver(ObjectProvider<Contract> contractObjectProvider) {
        this.contractObjectProvider = contractObjectProvider;
    }

    @Override
    public void afterSingletonsInstantiated() {

        LinkedList<Contract> contracts = new LinkedList<>();

        // Add injected Contract if available, for example SpringMvcContract Bean under Spring Cloud Open Feign
        Contract contract = contractObjectProvider.getIfAvailable();

        if (contract != null) {
            contracts.add(contract);
        }

        Stream.of(CONTRACT_CLASS_NAMES)
                .filter(this::isClassPresent) // filter the existed classes
                .map(this::loadContractClass) // load Contract Class
                .map(this::createContract)    // createServiceInstance instance by the specified class
                .forEach(contracts::add);     // add the Contract instance into contracts

        this.contracts = Collections.unmodifiableCollection(contracts);
    }

    private Contract createContract(Class<?> contractClassName) {
        return (Contract) BeanUtils.instantiateClass(contractClassName);
    }

    private Class<?> loadContractClass(String contractClassName) {
        return ClassUtils.resolveClassName(contractClassName, classLoader);
    }

    private boolean isClassPresent(String className) {
        return ClassUtils.isPresent(className, classLoader);
    }

    @Override
    public Set<ServiceRestMetadata> resolveServiceRestMetadata(ServiceBean serviceBean) {

        Object bean = serviceBean.getRef();

        Class<?> beanType = bean.getClass();

        Set<ServiceRestMetadata> serviceRestMetadata = new LinkedHashSet<>();

        Set<RestMethodMetadata> methodRestMetadata = resolveMethodRestMetadata(beanType);

        List<URL> urls = serviceBean.getExportedUrls();

        urls.stream()
                .map(URL::toString)
                .forEach(url -> {
                    ServiceRestMetadata metadata = new ServiceRestMetadata();
                    metadata.setUrl(url);
                    metadata.setMeta(methodRestMetadata);
                    serviceRestMetadata.add(metadata);
                });

        return serviceRestMetadata;
    }

    @Override
    public Set<RestMethodMetadata> resolveMethodRestMetadata(Class<?> targetType) {
        List<Method> feignContractMethods = selectFeignContractMethods(targetType);
        return contracts.stream()
                .map(contract -> parseAndValidateMetadata(contract, targetType))
                .flatMap(v -> v.stream())
                .map(methodMetadata -> resolveMethodRestMetadata(methodMetadata, targetType, feignContractMethods))
                .collect(Collectors.toSet());
    }

    private List<MethodMetadata> parseAndValidateMetadata(Contract contract, Class<?> targetType) {
        List<MethodMetadata> methodMetadataList = Collections.emptyList();
        try {
            methodMetadataList = contract.parseAndValidatateMetadata(targetType);
        } catch (Throwable ignored) {
            // ignore
        }
        return methodMetadataList;
    }

    /**
     * Select feign contract methods
     * <p>
     * extract some code from {@link Contract.BaseContract#parseAndValidatateMetadata(Class)}
     *
     * @param targetType
     * @return non-null
     */
    private List<Method> selectFeignContractMethods(Class<?> targetType) {
        List<Method> methods = new LinkedList<>();
        for (Method method : targetType.getMethods()) {
            if (method.getDeclaringClass() == Object.class ||
                    (method.getModifiers() & Modifier.STATIC) != 0 ||
                    Util.isDefault(method)) {
                continue;
            }
            methods.add(method);
        }
        return methods;
    }

    protected RestMethodMetadata resolveMethodRestMetadata(MethodMetadata methodMetadata, Class<?> targetType,
                                                           List<Method> feignContractMethods) {
        String configKey = methodMetadata.configKey();
        Method feignContractMethod = getMatchedFeignContractMethod(targetType, feignContractMethods, configKey);
        RestMethodMetadata metadata = new RestMethodMetadata(methodMetadata);
        metadata.setMethod(new org.springframework.cloud.alibaba.dubbo.metadata.MethodMetadata(feignContractMethod));
        return metadata;
    }

    private Method getMatchedFeignContractMethod(Class<?> targetType, List<Method> methods, String expectedConfigKey) {
        Method matchedMethod = null;
        for (Method method : methods) {
            String configKey = Feign.configKey(targetType, method);
            if (expectedConfigKey.equals(configKey)) {
                matchedMethod = method;
                break;
            }
        }
        return matchedMethod;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}