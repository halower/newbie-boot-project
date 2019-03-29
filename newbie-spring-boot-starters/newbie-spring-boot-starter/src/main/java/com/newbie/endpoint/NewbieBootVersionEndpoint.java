
package com.newbie.endpoint;

import lombok.extern.log4j.Log4j2;
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
 *
 * {@link org.springframework.core.io.support.PropertiesLoaderSupport#loadProperties(Properties)}
 */
@Log4j2
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
                log.warn("Load properties failed: {}", ex.getMessage());
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
        Assert.notNull(resourceLocation, "Properties resource location must not be null.");

        log.info("Loading properties file from {}", resourceLocation);
        Properties result = new Properties();
        try {
            PropertiesLoaderUtils.fillProperties(result, new EncodedResource(resourceLocation));
        } catch (IOException ex) {
            log.warn("Error occurred when loading properties from {}: {}", resourceLocation,
                ex.getMessage());
        }
        return result;
    }
}
