/*
 * 版权所有 (c) 2019-2029, halower (halower@foxmail.com).
 *
 * Apache 2.0 License 同时该协议为补充协议，不允许 996 工作制度企业使用该开源软件
 *
 * 反996许可证版本1.0
 *
 * 在符合下列条件的情况下，特此免费向任何得到本授权作品的副本（包括源代码、文件和/或相关内容，以下
 * 统称为“授权作品”）的个人和法人实体授权：被授权个人或法人实体有权以任何目的处置授权作品，包括但
 * 不限于使用、复制，修改，衍生利用、散布，发布和再许可：
 *
 * 1. 个人或法人实体必须在许可作品的每个再散布或衍生副本上包含以上版权声明和本许可证，不得自行修
 * 改。
 * 2. 个人或法人实体必须严格遵守与个人实际所在地或个人出生地或归化地、或法人实体注册地或经营地
 * （以较严格者为准）的司法管辖区所有适用的与劳动和就业相关法律、法规、规则和标准。如果该司法管辖
 * 区没有此类法律、法规、规章和标准或其法律、法规、规章和标准不可执行，则个人或法人实体必须遵守国
 * 际劳工标准的核心公约。
 * 3. 个人或法人不得以任何方式诱导、暗示或强迫其全职或兼职员工或其独立承包人以口头或书面形式同意直接或
 * 间接限制、削弱或放弃其所拥有的，受相关与劳动和就业有关的法律、法规、规则和标准保护的权利或补救
 * 措施，无论该等书面或口头协议是否被该司法管辖区的法律所承认，该等个人或法人实体也不得以任何方法
 * 限制其雇员或独立承包人向版权持有人或监督许可证合规情况的有关当局报告或投诉上述违反许可证的行为
 * 的权利。
 *
 * 该授权作品是"按原样"提供，不做任何明示或暗示的保证，包括但不限于对适销性、特定用途适用性和非侵
 * 权性的保证。在任何情况下，无论是在合同诉讼、侵权诉讼或其他诉讼中，版权持有人均不承担因本软件或
 * 本软件的使用或其他交易而产生、引起或与之相关的任何索赔、损害或其他责任。
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
                 .dlbm("测试开发者")
                 .rybm("5101001001")
                 .dwmc("测试单位")
                 .bmsah(_bmsah)
                 .dwbm("510100").build();
        currentUserContext.setBmsah(_bmsah);
        return currentUserContext;
    }
}


