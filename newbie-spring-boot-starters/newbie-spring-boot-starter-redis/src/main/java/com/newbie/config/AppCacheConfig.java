package com.newbie.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "newbie.cache")
public class AppCacheConfig {
    private String topic = "cache-topic";
}
