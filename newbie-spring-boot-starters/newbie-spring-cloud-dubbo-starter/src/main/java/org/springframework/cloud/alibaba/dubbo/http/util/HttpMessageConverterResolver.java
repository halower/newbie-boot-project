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
package org.springframework.cloud.alibaba.dubbo.http.util;

import org.springframework.cloud.alibaba.dubbo.http.converter.HttpMessageConverterHolder;
import org.springframework.cloud.alibaba.dubbo.metadata.RequestMetadata;
import org.springframework.cloud.alibaba.dubbo.metadata.RestMethodMetadata;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.unmodifiableList;

/**
 * {@link HttpMessageConverter} Resolver
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class HttpMessageConverterResolver {

    private static final MediaType MEDIA_TYPE_APPLICATION = new MediaType("application");

    private final List<HttpMessageConverter<?>> messageConverters;

    private final List<MediaType> allSupportedMediaTypes;

    private final ClassLoader classLoader;

    public HttpMessageConverterResolver(List<HttpMessageConverter<?>> messageConverters, ClassLoader classLoader) {
        this.messageConverters = messageConverters;
        this.allSupportedMediaTypes = getAllSupportedMediaTypes(messageConverters);
        this.classLoader = classLoader;
    }

    public HttpMessageConverterHolder resolve(HttpRequest request, Class<?> parameterType) {

        HttpMessageConverterHolder httpMessageConverterHolder = null;

        HttpHeaders httpHeaders = request.getHeaders();

        MediaType contentType = httpHeaders.getContentType();

        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        }

        for (HttpMessageConverter<?> converter : this.messageConverters) {
            if (converter instanceof GenericHttpMessageConverter) {
                GenericHttpMessageConverter genericConverter = (GenericHttpMessageConverter) converter;
                if (genericConverter.canRead(parameterType, parameterType, contentType)) {
                    httpMessageConverterHolder = new HttpMessageConverterHolder(contentType, converter);
                    break;
                }
            } else {
                if (converter.canRead(parameterType, contentType)) {
                    httpMessageConverterHolder = new HttpMessageConverterHolder(contentType, converter);
                    break;
                }
            }

        }

        return httpMessageConverterHolder;
    }

    /**
     * Resolve the most match {@link HttpMessageConverter} from {@link RequestMetadata}
     *
     * @param requestMetadata    {@link RequestMetadata}
     * @param restMethodMetadata {@link RestMethodMetadata}
     * @return
     */
    public HttpMessageConverterHolder resolve(RequestMetadata requestMetadata, RestMethodMetadata
            restMethodMetadata) {

        HttpMessageConverterHolder httpMessageConverterHolder = null;

        Class<?> returnValueClass = resolveReturnValueClass(restMethodMetadata);

        /**
         *  @see AbstractMessageConverterMethodProcessor#writeWithMessageConverters(Object, MethodParameter, ServletServerHttpRequest, ServletServerHttpResponse)
         */
        List<MediaType> requestedMediaTypes = getAcceptableMediaTypes(requestMetadata);
        List<MediaType> producibleMediaTypes = getProducibleMediaTypes(restMethodMetadata, returnValueClass);

        Set<MediaType> compatibleMediaTypes = new LinkedHashSet<MediaType>();
        for (MediaType requestedType : requestedMediaTypes) {
            for (MediaType producibleType : producibleMediaTypes) {
                if (requestedType.isCompatibleWith(producibleType)) {
                    compatibleMediaTypes.add(getMostSpecificMediaType(requestedType, producibleType));
                }
            }
        }

        if (compatibleMediaTypes.isEmpty()) {
            return httpMessageConverterHolder;
        }

        List<MediaType> mediaTypes = new ArrayList<>(compatibleMediaTypes);

        MediaType.sortBySpecificityAndQuality(mediaTypes);

        MediaType selectedMediaType = null;
        for (MediaType mediaType : mediaTypes) {
            if (mediaType.isConcrete()) {
                selectedMediaType = mediaType;
                break;
            } else if (mediaType.equals(MediaType.ALL) || mediaType.equals(MEDIA_TYPE_APPLICATION)) {
                selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
                break;
            }
        }

        if (selectedMediaType != null) {
            selectedMediaType = selectedMediaType.removeQualityValue();
            for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
                if (messageConverter.canWrite(returnValueClass, selectedMediaType)) {
                    httpMessageConverterHolder = new HttpMessageConverterHolder(selectedMediaType, messageConverter);
                    break;
                }
            }
        }

        return httpMessageConverterHolder;
    }

    public List<MediaType> getAllSupportedMediaTypes() {
        return unmodifiableList(allSupportedMediaTypes);
    }

    private Class<?> resolveReturnValueClass(RestMethodMetadata restMethodMetadata) {
        String returnClassName = restMethodMetadata.getMethod().getReturnType();
        return ClassUtils.resolveClassName(returnClassName, classLoader);
    }

    /**
     * Resolve the {@link MediaType media-types}
     *
     * @param requestMetadata {@link RequestMetadata} from client side
     * @return non-null {@link List}
     */
    private List<MediaType> getAcceptableMediaTypes(RequestMetadata requestMetadata) {
        return requestMetadata.getProduceMediaTypes();
    }

    /**
     * Returns
     * the media types that can be produced: <ul> <li>The producible media types specified in the request mappings, or
     * <li>Media types of configured converters that can write the specific return value, or <li>{@link MediaType#ALL}
     * </ul>
     *
     * @param restMethodMetadata {@link RestMethodMetadata} from server side
     * @param returnValueClass   the class of return value
     * @return non-null {@link List}
     */
    private List<MediaType> getProducibleMediaTypes(RestMethodMetadata restMethodMetadata, Class<?>
            returnValueClass) {
        RequestMetadata serverRequestMetadata = restMethodMetadata.getRequest();
        List<MediaType> mediaTypes = serverRequestMetadata.getProduceMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) { // Empty
            return mediaTypes;
        } else if (!this.allSupportedMediaTypes.isEmpty()) {
            List<MediaType> result = new ArrayList<>();
            for (HttpMessageConverter<?> converter : this.messageConverters) {
                if (converter.canWrite(returnValueClass, null)) {
                    result.addAll(converter.getSupportedMediaTypes());
                }
            }
            return result;
        } else {
            return Collections.singletonList(MediaType.ALL);
        }
    }

    /**
     * Return the media types
     * supported by all provided message converters sorted by specificity via {@link
     * MediaType#sortBySpecificity(List)}.
     *
     * @param messageConverters
     * @return
     */
    private List<MediaType> getAllSupportedMediaTypes(List<HttpMessageConverter<?>> messageConverters) {
        Set<MediaType> allSupportedMediaTypes = new LinkedHashSet<MediaType>();
        for (HttpMessageConverter<?> messageConverter : messageConverters) {
            allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
        }
        List<MediaType> result = new ArrayList<MediaType>(allSupportedMediaTypes);
        MediaType.sortBySpecificity(result);
        return unmodifiableList(result);
    }

    /**
     * Return the more specific of the acceptable and the producible media types
     * with the q-value of the former.
     */
    private MediaType getMostSpecificMediaType(MediaType acceptType, MediaType produceType) {
        MediaType produceTypeToUse = produceType.copyQualityValue(acceptType);
        return (MediaType.SPECIFICITY_COMPARATOR.compare(acceptType, produceTypeToUse) <= 0 ? acceptType : produceTypeToUse);
    }
}
