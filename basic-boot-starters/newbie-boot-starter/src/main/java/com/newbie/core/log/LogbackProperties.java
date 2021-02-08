package com.newbie.core.log;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Date: 2021/2/5  14:13
 **/
@Data
@ConfigurationProperties("application.logback")
public class LogbackProperties {
    // 是否使用文件保存
    private Boolean useFile = false;
    // 是否使用数据库存储
    private Boolean useDb = false;
    // 文件存储路径
    private String  filePath = "/logs";
}
