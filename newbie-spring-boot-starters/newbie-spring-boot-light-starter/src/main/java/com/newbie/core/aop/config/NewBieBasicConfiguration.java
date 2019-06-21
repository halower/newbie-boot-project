package com.newbie.core.aop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class NewBieBasicConfiguration {
    private String env = "dev";

    private String authSecretKey="1b6ba62cff8629865a6b9dffa1586d74";

    private List<String> excludeFilterPath = Arrays.asList(
            "/api/org/unit/getUnitTree",
            "/api/org/user/login");
    @Deprecated
    private boolean autoRecordRequestDetails = false;
    @Deprecated
    private boolean receiveRequestEvent = false;
}
