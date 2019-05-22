package com.newbie.core.aop;

import com.alibaba.fastjson.JSON;
import com.newbie.core.aop.config.NewBieBasicConfiguration;
import com.newbie.core.audit.CurrentUserContext;
import com.newbie.core.exception.BusinessException;
import com.newbie.core.utils.env.UserInfoManager;
import com.newbie.core.utils.env.NewBieBootEnvUtils;
import com.newbie.dto.ResponseTypes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.java.Log;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 谢海龙
 * @Date: 2019/5/22 13:40
 * @Description
 */
@Configuration
@Component
@Log
public class UserInfoForWebFilter extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String prefix = "Bearer ";
        final String dev = "dev";
        final String header = "Authorization";
        CurrentUserContext currentUserInfo;
        var basicConfiguration = NewBieBootEnvUtils.getBean(NewBieBasicConfiguration.class);
        String authorization = request.getHeader(header);
        if (null == authorization && basicConfiguration.getEnv().equals(dev)) {
            currentUserInfo = CurrentUserContext.builder()
                    .dlbm("开发者")
                    .rybm("5101001001")
                    .dwmc("测试单位")
                    .dwbm("510100").build();
        } else {
            if (null != authorization && !StringUtils.startsWithIgnoreCase(authorization, prefix)) {
                throw new BusinessException(ResponseTypes.TOKEN_UNVALID);
            }
            String token = request.getHeader(header).replace(prefix, "").trim();
            Claims claims = Jwts.parser()
                    .setSigningKey(basicConfiguration.getAuthSecretKey()).parseClaimsJws(token)
                    .getBody();
            currentUserInfo = JSON.parseObject(claims.getSubject(), CurrentUserContext.class);
        }
        UserInfoManager.getInstance().bind(currentUserInfo);
        RpcContext.getContext().setAttachment("currentUserInfo",JSON.toJSONString(currentUserInfo));
        return true;
    }
}
