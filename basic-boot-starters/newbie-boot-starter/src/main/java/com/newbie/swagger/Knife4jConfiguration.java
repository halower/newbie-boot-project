package com.newbie.swagger;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.Charset;

@EnableSwagger2
public class Knife4jConfiguration {
    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Autowired
    private SwaggerProperties swaggerProperties;

    public Knife4jConfiguration(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title(StringUtils.toEncodedString(swaggerProperties.getTitle().getBytes(), Charset.forName("UTF-8")))
                        .description(StringUtils.toEncodedString(swaggerProperties.getDescription().getBytes(),Charset.forName("UTF-8")))
                        .version(StringUtils.toEncodedString(swaggerProperties.getVersion().getBytes(),Charset.forName("UTF-8")))
                        .build())
                //分组名称
                .groupName(StringUtils.toEncodedString(swaggerProperties.getGroupName().getBytes(),Charset.forName("UTF-8")))
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildSettingExtensions());
        return docket;
    }
}