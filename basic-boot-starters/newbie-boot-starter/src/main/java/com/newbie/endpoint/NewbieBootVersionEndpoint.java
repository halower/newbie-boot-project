///*
// * Apache License
// *
// * Copyright (c) 2019  halower (halower@foxmail.com).
// *
// * <p>
// * Licensed under the Apache License, Version 2.0 (the "License"); you may not
// * use this file except in compliance with the License. You may obtain a copy of
// * the License at
// * <p>
// * https://www.apache.org/licenses/LICENSE-2.0
// * <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// * License for the specific language governing permissions and limitations under
// * the License.
// */
//
package com.newbie.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.newbie.constants.NewbieBootInfraConstants.NEWBIE_BOOT_VERSION_PROPERTIES;

/**
 * NEWBIEBootVersionEndpoint
 */
@Slf4j
@Endpoint(id = "versions")
public class NewbieBootVersionEndpoint {
    private List<Object>  endpointResult = new ArrayList<>();
    private PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    @ReadOperation
    public List<Object> versions() {
        if (endpointResult.isEmpty()) {
            try {
                endpointResult = Stream
                        .of(resourcePatternResolver.getResources(NEWBIE_BOOT_VERSION_PROPERTIES))
                        .map(this::loadProperties).collect(Collectors.toList());
            } catch (Exception ex) {
                log.error("加载properties文件失败: {}", ex.getMessage());
            }
        }
        return endpointResult;
    }

    /**
     * Load properties into the given newbie.versions.properties resource.
     *
     * @param resourceLocation the resource locations to load
     */
    private Properties loadProperties(Resource resourceLocation) {
        Assert.notNull(resourceLocation, "properties 资源不能为空.");

        log.error("加载properties文件失败，位置： {}", resourceLocation);
        Properties result = new Properties();
        try {
            PropertiesLoaderUtils.fillProperties(result, new EncodedResource(resourceLocation));
        } catch (IOException ex) {
            log.error("加载 properties文件失败{}: 原因{}", resourceLocation, ex.getMessage());
        }
        return result;
    }
}
