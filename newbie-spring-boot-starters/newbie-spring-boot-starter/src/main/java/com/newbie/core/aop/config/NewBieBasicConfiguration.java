package com.newbie.core.aop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "newbie")
public class NewBieBasicConfiguration {
    private String env = "dev";
    private boolean autoRecordRequestDetails = false;
    private boolean receiveRequestEvent = false;
    private String auditorTagInHead="USER-ID";
}
