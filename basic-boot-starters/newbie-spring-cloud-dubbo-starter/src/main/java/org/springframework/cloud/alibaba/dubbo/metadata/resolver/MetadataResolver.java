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

package org.springframework.cloud.alibaba.dubbo.metadata.resolver;

import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.cloud.alibaba.dubbo.metadata.RestMethodMetadata;
import org.springframework.cloud.alibaba.dubbo.metadata.ServiceRestMetadata;

import java.util.Set;

/**
 * The REST aop resolver
 *
 * @Author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public interface MetadataResolver {

    /**
     * Resolve the {@link ServiceRestMetadata} {@link Set set} from {@link ServiceBean}
     *
     * @param serviceBean {@link ServiceBean}
     * @return non-null {@link Set}
     */
    Set<ServiceRestMetadata> resolveServiceRestMetadata(ServiceBean serviceBean);

    /**
     * Resolve {@link RestMethodMetadata} {@link Set set} from {@link Class target type}
     *
     * @param targetType {@link Class target type}
     * @return non-null {@link Set}
     */
    Set<RestMethodMetadata> resolveMethodRestMetadata(Class<?> targetType);
}
