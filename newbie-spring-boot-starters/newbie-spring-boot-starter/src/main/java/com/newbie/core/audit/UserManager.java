package com.newbie.core.audit;

import com.alibaba.fastjson.JSON;
import com.newbie.core.aop.config.NewBieBasicConfiguration;
import com.newbie.core.audit.CurrentUserInfo;
import com.newbie.core.utils.env.NewBieBootEnvUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author: 谢海龙
 * @Date: 2019/5/15 19:50
 * @Description
 */
public class UserManager {
    public static CurrentUserInfo getHttpRequestUserInfo() {
        final String PREFIX = "Bearer ";
        var basicConfiguration = NewBieBootEnvUtils.getBean(NewBieBasicConfiguration.class);
        var request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader("Authorization");
        if(null == authorization && basicConfiguration.getEnv().equals("dev")){
            return CurrentUserInfo.builder()
                    .dlbm("开发者")
                    .rybm("5101001001")
                    .dwmc("测试单位")
                    .dwbm("510100").build();
        } else{
            if (null != authorization && !StringUtils.startsWithIgnoreCase(authorization, PREFIX)) {
                return  null;
            }
            String token = request.getHeader("Authorization").replace(PREFIX, "").trim();
            Claims claims = Jwts.parser()
                    .setSigningKey(basicConfiguration.getAuthSecretKey()).parseClaimsJws(token)
                    .getBody();
            CurrentUserInfo currentUserInfo = JSON.parseObject(claims.getSubject(), CurrentUserInfo.class);
            return currentUserInfo;
    }
}

    public static CurrentUserInfo getRpcRequestUserInfo() {
       var isProviderSide = RpcContext.getContext().isProviderSide();
       if(!isProviderSide) return null;
       var basicConfiguration = NewBieBootEnvUtils.getBean(NewBieBasicConfiguration.class);
       var userInfo =  RpcContext.getContext().get("currentUserInfo");
       if(userInfo!=null) {
           return  (CurrentUserInfo)userInfo;
       }
       if(basicConfiguration.getEnv().equals("dev")){
           return CurrentUserInfo.builder()
                   .dlbm("开发者")
                   .rybm("5101001001")
                   .dwmc("测试单位")
                   .dwbm("510100").build();
       }
        return null;
    }

    public static void setRpcContextUserInfo(CurrentUserInfo currentUserInfo) {
        RpcContext.getContext().set("currentUserInfo",currentUserInfo);
    }

    public static CurrentUserInfo getUserInfo() {
        if(null!=RequestContextHolder.getRequestAttributes()) {
            return getHttpRequestUserInfo();
        }
        return getRpcRequestUserInfo();
    }
}