package com.newbie.core.datasource.crypt;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;


@Data
public class DssResultDTO {
    @JSONField(name = "Plain")
    private String plain;
    @JSONField(name = "RequestID")
    private String requestID;
}
