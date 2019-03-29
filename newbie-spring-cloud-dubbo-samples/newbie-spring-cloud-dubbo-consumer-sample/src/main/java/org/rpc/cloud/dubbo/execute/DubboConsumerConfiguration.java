package org.rpc.cloud.dubbo.execute;

import org.springframework.cloud.alibaba.dubbo.annotation.DubboTransported;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DubboConsumerConfiguration {

    @LoadBalanced
    @DubboTransported
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
