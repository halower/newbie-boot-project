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
package org.springframework.cloud.alibaba.dubbo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.dubbo.http.HttpServerRequest;
import org.springframework.cloud.alibaba.dubbo.metadata.MethodMetadata;
import org.springframework.cloud.alibaba.dubbo.metadata.MethodParameterMetadata;
import org.springframework.cloud.alibaba.dubbo.metadata.RestMethodMetadata;
import org.springframework.cloud.alibaba.dubbo.service.parameter.DubboGenericServiceParameterResolver;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link DubboGenericServiceExecutionContext} Factory
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see DubboGenericServiceParameterResolver
 */
public class DubboGenericServiceExecutionContextFactory {

    @Autowired(required = false)
    private final List<DubboGenericServiceParameterResolver> resolvers = Collections.emptyList();

    @PostConstruct
    public void init() {
        AnnotationAwareOrderComparator.sort(resolvers);
    }

    public DubboGenericServiceExecutionContext create(RestMethodMetadata dubboRestMethodMetadata,
                                                      RestMethodMetadata clientMethodMetadata, Object[] arguments) {

        MethodMetadata dubboMethodMetadata = dubboRestMethodMetadata.getMethod();

        String methodName = dubboMethodMetadata.getName();

        String[] parameterTypes = resolveParameterTypes(dubboMethodMetadata);

        Object[] parameters = resolveParameters(dubboRestMethodMetadata, clientMethodMetadata, arguments);

        return new DubboGenericServiceExecutionContext(methodName, parameterTypes, parameters);
    }

    public DubboGenericServiceExecutionContext create(RestMethodMetadata dubboRestMethodMetadata,
                                                      HttpServerRequest request) {
        MethodMetadata methodMetadata = dubboRestMethodMetadata.getMethod();

        String methodName = methodMetadata.getName();

        String[] parameterTypes = resolveParameterTypes(methodMetadata);

        Object[] parameters = resolveParameters(dubboRestMethodMetadata, request);

        return new DubboGenericServiceExecutionContext(methodName, parameterTypes, parameters);
    }

    private Map<String, Integer> buildParamNameToIndex(List<MethodParameterMetadata> params) {
        Map<String, Integer> paramNameToIndex = new LinkedHashMap<>();
        for (MethodParameterMetadata param : params) {
            paramNameToIndex.put(param.getName(), param.getIndex());
        }
        return paramNameToIndex;
    }

    protected String[] resolveParameterTypes(MethodMetadata methodMetadata) {

        List<MethodParameterMetadata> params = methodMetadata.getParams();

        String[] parameterTypes = new String[params.size()];

        for (MethodParameterMetadata parameterMetadata : params) {
            int index = parameterMetadata.getIndex();
            String parameterType = parameterMetadata.getType();
            parameterTypes[index] = parameterType;
        }

        return parameterTypes;
    }

    protected Object[] resolveParameters(RestMethodMetadata dubboRestMethodMetadata, HttpServerRequest request) {

        MethodMetadata dubboMethodMetadata = dubboRestMethodMetadata.getMethod();

        List<MethodParameterMetadata> params = dubboMethodMetadata.getParams();

        Object[] parameters = new Object[params.size()];

        for (MethodParameterMetadata parameterMetadata : params) {

            int index = parameterMetadata.getIndex();

            for (DubboGenericServiceParameterResolver resolver : resolvers) {
                Object parameter = resolver.resolve(dubboRestMethodMetadata, parameterMetadata, request);
                if (parameter != null) {
                    parameters[index] = parameter;
                    break;
                }
            }
        }

        return parameters;
    }

    protected Object[] resolveParameters(RestMethodMetadata dubboRestMethodMetadata,
                                         RestMethodMetadata clientRestMethodMetadata, Object[] arguments) {

        MethodMetadata dubboMethodMetadata = dubboRestMethodMetadata.getMethod();

        List<MethodParameterMetadata> params = dubboMethodMetadata.getParams();

        Object[] parameters = new Object[params.size()];

        for (MethodParameterMetadata parameterMetadata : params) {

            int index = parameterMetadata.getIndex();

            for (DubboGenericServiceParameterResolver resolver : resolvers) {
                Object parameter = resolver.resolve(dubboRestMethodMetadata, parameterMetadata, clientRestMethodMetadata, arguments);
                if (parameter != null) {
                    parameters[index] = parameter;
                    break;
                }
            }
        }

        return parameters;
    }
}
