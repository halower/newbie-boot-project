package com.newbie.swagger;

import com.newbie.core.config.NewbieBootBasicProperties;
import com.newbie.swagger.spi.SwaggerReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("api/swagger")
public class SwaggerController {
    @Autowired
    NewbieBootBasicProperties configuration;
    @Autowired(required = false)
    private SwaggerReader swaggerReader;

    @GetMapping("tio")
    public Map<String, Object> listTryItOutPaths() {
        Map<String, Object> res = new HashMap<>();
        res.put("env", configuration.getEnv());
        if(swaggerReader !=null) {
            res.put("data", swaggerReader.listTryItOutPaths());
            return res;
        }
        return res;
    }
}
