package com.newbie.core.audit;

import com.newbie.core.event.EventData;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class BusinessEventData extends FullAudited  implements EventData{
    /***
     * 请求路径
     */
    private String requestUrl;

    /**
     * 请求谓词
     */
    private String requestMethod;

    /**
     * 请求Ip
     */
    private String requestIp;

    /**
     * 调用方法
     */
    private String callMethod;

    /**
     * 描述
     */
    private String description;

    /**
     * 传入参数
     */
    private String args;

    /**
     * 返回参数
     */
    private String retrunVal;

    /**
     * 响应时间
     */
    private String offsetTime;
}
