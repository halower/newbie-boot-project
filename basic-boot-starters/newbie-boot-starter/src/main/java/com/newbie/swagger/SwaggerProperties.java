package com.newbie.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Date: 2021/2/5  14:13
 **/
@Data
@ConfigurationProperties("application.swagger")
public class SwaggerProperties {
    private String title = "开发文档";
    private String description = "开发文档";
    private String version = "1.0";
    private String groupName = "开发小组";
}
