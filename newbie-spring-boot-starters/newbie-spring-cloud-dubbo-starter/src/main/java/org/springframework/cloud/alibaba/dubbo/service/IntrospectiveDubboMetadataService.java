
package org.springframework.cloud.alibaba.dubbo.service;

import org.apache.dubbo.common.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.dubbo.metadata.ServiceRestMetadata;
import org.springframework.cloud.alibaba.dubbo.metadata.repository.DubboServiceMetadataRepository;
import org.springframework.cloud.alibaba.dubbo.util.JSONUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.unmodifiableMap;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Introspective {@link DubboMetadataService} implementation
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
public class IntrospectiveDubboMetadataService implements DubboMetadataService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectProvider<DubboServiceMetadataRepository> dubboServiceMetadataRepository;

    @Autowired
    private JSONUtils jsonUtils;

    @Override
    public String getServiceRestMetadata() {
        Set<ServiceRestMetadata> serviceRestMetadata = getRepository().getServiceRestMetadata();
        String serviceRestMetadataJsonConfig = null;
        if (!isEmpty(serviceRestMetadata)) {
            serviceRestMetadataJsonConfig = jsonUtils.toJSON(serviceRestMetadata);
        }
        return serviceRestMetadataJsonConfig;
    }

    @Override
    public Set<String> getAllServiceKeys() {
        return getRepository().getAllServiceKeys();
    }

    @Override
    public Map<String, String> getAllExportedURLs() {
        Map<String, List<URL>> allExportedUrls = getRepository().getAllExportedUrls();
        if (isEmpty(allExportedUrls)) {
            if (logger.isDebugEnabled()) {
                logger.debug("There is no registered URL.");
            }
            return Collections.emptyMap();
        }

        Map<String, String> result = new HashMap<>();

        allExportedUrls.forEach((serviceKey, urls) -> {
            result.put(serviceKey, jsonUtils.toJSON(urls));
        });

        return unmodifiableMap(result);
    }

    @Override
    public String getExportedURLs(String serviceInterface, String group, String version) {
        List<URL> urls = getRepository().getExportedURLs(serviceInterface, group, version);
        return jsonUtils.toJSON(urls);
    }

    private DubboServiceMetadataRepository getRepository() {
        return dubboServiceMetadataRepository.getIfAvailable();
    }
}
