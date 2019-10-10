/*
 * Apache License
 *
 * Copyright (c) 2019  halower (halower@foxmail.com).
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.newbie.core.aop;

import com.alibaba.fastjson.JSON;
import com.newbie.constants.NewbieBootInfraConstants;
import com.newbie.core.aop.config.NewBieBasicConfiguration;
import com.newbie.context.CurrentUserContext;
import com.newbie.core.exception.BusinessException;
import com.newbie.context.UserInfoManager;
import com.newbie.context.NewBieBootEnvUtil;
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

/**
 * @author: halower
 * @date: 2019/5/22 13:40
 *
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
                var basicConfiguration = NewBieBootEnvUtil.getBean(NewBieBasicConfiguration.class);
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
                 .dlbm("第三方")
                 .rybm("C000000001")
                 .dwmc("第三方单位")
                 .bmsah(_bmsah)
                 .dwbm("C00001").build();
        currentUserContext.setBmsah(_bmsah);
        return currentUserContext;
    }
}


