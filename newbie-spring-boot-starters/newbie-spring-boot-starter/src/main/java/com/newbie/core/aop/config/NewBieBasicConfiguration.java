package com.newbie.core.aop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "application")
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

    private DubboConfig dubbo;

    @Data
    public static class DubboConfig {
        private ConsumerConfig consumer;
        private ProviderConfig provider;

        @Data
        public static class ProviderConfig {
            private String version ="1.0.0";
        }

        @Data
        public static class ConsumerConfig {
            private String version ="1.0.0";
        }
    }
}
