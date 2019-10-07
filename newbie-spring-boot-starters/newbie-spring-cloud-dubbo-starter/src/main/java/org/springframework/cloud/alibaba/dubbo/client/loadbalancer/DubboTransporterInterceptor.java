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
package org.springframework.cloud.alibaba.dubbo.client.loadbalancer;

import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;

import org.springframework.cloud.alibaba.dubbo.http.MutableHttpServerRequest;
import org.springframework.cloud.alibaba.dubbo.metadata.DubboRestServiceMetadata;
import org.springframework.cloud.alibaba.dubbo.metadata.RequestMetadata;
import org.springframework.cloud.alibaba.dubbo.metadata.RestMethodMetadata;
import org.springframework.cloud.alibaba.dubbo.metadata.repository.DubboServiceMetadataRepository;
import org.springframework.cloud.alibaba.dubbo.service.DubboGenericServiceExecutionContext;
import org.springframework.cloud.alibaba.dubbo.service.DubboGenericServiceExecutionContextFactory;
import org.springframework.cloud.alibaba.dubbo.service.DubboGenericServiceFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UriComponents;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.web.util.UriComponentsBuilder.fromUri;

/**
 * Dubbo Transporter {@link ClientHttpRequestInterceptor} implementation
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see LoadBalancerInterceptor
 */
public class DubboTransporterInterceptor implements ClientHttpRequestInterceptor {

    private final DubboServiceMetadataRepository repository;

    private final DubboClientHttpResponseFactory clientHttpResponseFactory;

    private final Map<String, Object> dubboTranslatedAttributes;

    private final DubboGenericServiceFactory serviceFactory;

    private final DubboGenericServiceExecutionContextFactory contextFactory;

    private final PathMatcher pathMatcher = new AntPathMatcher();

    public DubboTransporterInterceptor(DubboServiceMetadataRepository dubboServiceMetadataRepository,
                                       List<HttpMessageConverter<?>> messageConverters,
                                       ClassLoader classLoader,
                                       Map<String, Object> dubboTranslatedAttributes,
                                       DubboGenericServiceFactory serviceFactory,
                                       DubboGenericServiceExecutionContextFactory contextFactory) {
        this.repository = dubboServiceMetadataRepository;
        this.dubboTranslatedAttributes = dubboTranslatedAttributes;
        this.clientHttpResponseFactory = new DubboClientHttpResponseFactory(messageConverters, classLoader);
        this.serviceFactory = serviceFactory;
        this.contextFactory = contextFactory;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        URI originalUri = request.getURI();

        String serviceName = originalUri.getHost();

        RequestMetadata clientMetadata = buildRequestMetadata(request);

        DubboRestServiceMetadata metadata = repository.get(serviceName, clientMetadata);

        if (metadata == null) {
            // if DubboServiceMetadata is not found, executes next
            return execution.execute(request, body);
        }

        RestMethodMetadata dubboRestMethodMetadata = metadata.getRestMethodMetadata();

        GenericService genericService = serviceFactory.create(metadata, dubboTranslatedAttributes);

        MutableHttpServerRequest httpServerRequest = new MutableHttpServerRequest(request, body);

        customizeRequest(httpServerRequest, dubboRestMethodMetadata, clientMetadata);

        DubboGenericServiceExecutionContext context = contextFactory.create(dubboRestMethodMetadata, httpServerRequest);

        Object result = null;
        GenericException exception = null;

        try {
            result = genericService.$invoke(context.getMethodName(), context.getParameterTypes(), context.getParameters());
        } catch (GenericException e) {
            exception = e;
        }

        return clientHttpResponseFactory.build(result, exception, clientMetadata, dubboRestMethodMetadata);
    }

    protected void customizeRequest(MutableHttpServerRequest httpServerRequest,
                                    RestMethodMetadata dubboRestMethodMetadata, RequestMetadata clientMetadata) {

        RequestMetadata dubboRequestMetadata = dubboRestMethodMetadata.getRequest();
        String pathPattern = dubboRequestMetadata.getPath();

        Map<String, String> pathVariables = pathMatcher.extractUriTemplateVariables(pathPattern, httpServerRequest.getPath());

        if (!CollectionUtils.isEmpty(pathVariables)) {
            // Put path variables Map into query parameters Map
            httpServerRequest.params(pathVariables);
        }

    }

    private RequestMetadata buildRequestMetadata(HttpRequest request) {
        UriComponents uriComponents = fromUri(request.getURI()).build(true);
        RequestMetadata requestMetadata = new RequestMetadata();
        requestMetadata.setPath(uriComponents.getPath());
        requestMetadata.setMethod(request.getMethod().name());
        requestMetadata.setParams(uriComponents.getQueryParams());
        requestMetadata.setHeaders(request.getHeaders());
        return requestMetadata;
    }
}
