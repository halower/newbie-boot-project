package com.newbie.core.aop;

import com.alibaba.fastjson.JSON;
import com.newbie.constants.NewbieBootInfraConstants;
import com.newbie.core.aop.config.NewBieBasicConfiguration;
import com.newbie.core.audit.CurrentUserContext;
import com.newbie.core.exception.BusinessException;
import com.newbie.core.utils.env.UserInfoManager;
import com.newbie.core.utils.env.NewBieBootEnvUtils;
import com.newbie.dto.ResponseTypes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @Author: 谢海龙
 * @Date: 2019/5/22 13:40
 * @Description
 */
@Configuration
@Component
@Log4j2
public class UserInfoForWebFilter implements Filter {
    @Autowired
    private NewBieBasicConfiguration configuration;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()).replaceAll("[/]+$", "");
        boolean allowedPath = configuration.getExcludeFilterPath().contains(path);
        if(allowedPath) {
            chain.doFilter(request,response);
        } else{
            try {
                final String prefix = "Bearer ";
                final String dev = "dev";
                final String authHeader = "Authorization";
                final String bmsahHeader = "X-BMSAH";
                var basicConfiguration = NewBieBootEnvUtils.getBean(NewBieBasicConfiguration.class);
                String authorization = httpRequest.getHeader(authHeader);
                String bmsah = httpRequest.getHeader(bmsahHeader);
                if(basicConfiguration.getEnv().equals(dev)) {
                    if(null == authorization ) {
                        bindFormDefaultValue(bmsah);
                        chain.doFilter(request,response);
                    } else {
                        if(!StringUtils.startsWithIgnoreCase(authorization, prefix)){
                            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,ResponseTypes.TOKEN_UNVALID.getDesc());
                        }
                        bindUserInfoForToken(httpRequest, prefix, authHeader, basicConfiguration, bmsah);
                        chain.doFilter(request,response);
                    }
                }
                else  {
                    if(null == authorization || !StringUtils.startsWithIgnoreCase(authorization, prefix)){
                        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,ResponseTypes.TOKEN_UNVALID.getDesc());
                    }
                    bindUserInfoForToken(httpRequest, prefix, authHeader, basicConfiguration, bmsah);
                    chain.doFilter(request,response);
                }
            }catch (Exception ex) {
                ex.printStackTrace();
                log.error(ex.getMessage());
            }finally {
                UserInfoManager.getInstance().remove();
            }
        }
    }

    private void bindFormDefaultValue(String _bmsah) {
        CurrentUserContext currentUserContext;
        currentUserContext = defaultUserInfo(_bmsah);
        UserInfoManager.getInstance().bind(currentUserContext);
        RpcContext.getContext().setAttachment(NewbieBootInfraConstants.CURRENT_USER_INFO,JSON.toJSONString(currentUserContext));
    }

    private void bindUserInfoForToken(HttpServletRequest httpRequest, String prefix, String authHeader, NewBieBasicConfiguration basicConfiguration, String bmsah) {
        CurrentUserContext currentUserContext;
        String token = httpRequest.getHeader(authHeader).replace(prefix, "").trim();
        Claims claims;
        try{
             claims = Jwts.parser()
                    .setSigningKey(basicConfiguration.getAuthSecretKey()).parseClaimsJws(token)
                    .getBody();
        } catch (JwtException ex) {
            log.error(ex.getMessage());
            throw new BusinessException(ResponseTypes.UNAUTHORIZED);
        }
        currentUserContext = JSON.parseObject(claims.getSubject(), CurrentUserContext.class);
        currentUserContext.setBmsah(bmsah);
        UserInfoManager.getInstance().bind(currentUserContext);
        RpcContext.getContext().setAttachment(NewbieBootInfraConstants.CURRENT_USER_INFO,JSON.toJSONString(currentUserContext));
    }
    private CurrentUserContext defaultUserInfo(String _bmsah) {
        CurrentUserContext currentUserContext;
        currentUserContext = CurrentUserContext.builder()
                 .dlbm("测试开发者")
                 .rybm("5101001001")
                 .dwmc("测试单位")
                 .bmsah(_bmsah)
                 .dwbm("510100").build();
        currentUserContext.setBmsah(_bmsah);
        return currentUserContext;
    }
}


