package org.rpc.cloud.dubbo.execute;

import org.apache.dubbo.config.annotation.Reference;
import org.rpc.cloud.dubbo.service.EchoService;
import org.rpc.cloud.dubbo.service.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@RestController
public class DubboController {

    @Reference(version = "1.0.0")
    private EchoService echoService;

    @Autowired
    @Lazy
    private DubboFeignEchoService dubboFeignEchoService;

    @Autowired
    @Lazy
    private FeignEchoService feignEchoService;

    @Autowired
    private RestTemplate restTemplate;
    private String serviceName = "spring-cloud-dubbo-sample-provider";

    @GetMapping("/dubbofeign")
    public String dubbofeign() {
        return dubboFeignEchoService.echo("by dubbofeign");
    }


    @GetMapping("/dubbofeign2")
    public User dubbofeign2() {
        User user = new User();
        user.setId(1L);
        user.setName("halower");
        user.setAge(11);
        return dubboFeignEchoService.requestBodyUser(user);
    }

    @GetMapping("/dubbo")
    public String dubbo() {
        return echoService.echo("by dubbo");
    }

//    @GetMapping("/dubbo2")
//    public User dubbo2() {
//        User user = new User();
//        user.setId(1L);
//        user.setName("halower");
//        user.setAge(11);
//        return echoService.requestBodyUser(user);
//    }
    @GetMapping("/feign")
    public String feign() {
        return feignEchoService.echo("by feign");
    }

    @GetMapping("/template")
    public String template() {
        return restTemplate.getForObject("http://" + serviceName + "/echo?param={p}", String.class, "by resttemplate");
    }

}
