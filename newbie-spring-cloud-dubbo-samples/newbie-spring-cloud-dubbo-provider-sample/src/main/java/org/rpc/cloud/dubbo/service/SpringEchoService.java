package org.rpc.cloud.dubbo.service;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Service(version = "1.0.0")
@RestController
public class SpringEchoService implements EchoService {

    @Override
    @GetMapping(value = "/echo")
    public String echo(@RequestParam String param) {
        System.out.println("SpringEchoService dubbo echo invoke: " + param);
        return "Spring Cloud Dubbo echo: " + param;
    }

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User requestBodyUser(@RequestBody User user) {
       return user;
    }
}
