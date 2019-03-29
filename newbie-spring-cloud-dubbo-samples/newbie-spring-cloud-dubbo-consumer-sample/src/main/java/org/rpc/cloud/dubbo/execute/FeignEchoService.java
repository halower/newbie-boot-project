package org.rpc.cloud.dubbo.execute;

import org.rpc.cloud.dubbo.service.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@FeignClient("spring-cloud-dubbo-sample-provider")
public interface FeignEchoService {
    @GetMapping(value = "/echo")
    String echo(@RequestParam("param") String param);

    @PostMapping(value = "/user",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    User requestBodyUser(@RequestBody User user);
}
