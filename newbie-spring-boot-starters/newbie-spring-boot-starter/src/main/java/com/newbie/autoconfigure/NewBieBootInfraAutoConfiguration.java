package com.newbie.autoconfigure;

import com.newbie.endpoint.VersionEndpointConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ VersionEndpointConfiguration.class })
public class NewBieBootInfraAutoConfiguration {
}

