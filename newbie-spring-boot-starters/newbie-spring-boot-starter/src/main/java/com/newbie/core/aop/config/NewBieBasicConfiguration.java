package com.newbie.core.aop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class NewBieBasicConfiguration {
    private String env = "dev";
    private String authSecretKey="tyyw";
    @Deprecated
    private boolean autoRecordRequestDetails = false;
    @Deprecated
    private boolean receiveRequestEvent = false;
}
