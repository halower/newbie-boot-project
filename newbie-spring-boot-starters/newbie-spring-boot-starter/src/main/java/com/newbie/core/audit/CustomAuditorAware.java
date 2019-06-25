package com.newbie.core.audit;

import com.alibaba.fastjson.JSON;
import com.newbie.context.CurrentUserContext;
import com.newbie.core.aop.config.NewBieBasicConfiguration;
import com.newbie.core.exception.BusinessException;
import com.newbie.dto.ResponseTypes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Configuration
public class CustomAuditorAware implements AuditorAware<String> {
    @Autowired
    private NewBieBasicConfiguration configuration;
    private static final String PREFIX = "Bearer ";
    @Override
    @SneakyThrows
    public Optional<String> getCurrentAuditor() {
        //本项目不需要
        return Optional.of("");
    }

    private Optional<String> getCurrentUserBm() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader("Authorization");
        if(null == authorization && configuration.getEnv().equals("dev")){
            return Optional.of("123456789");
        } else {
            if (null != authorization && !StringUtils.startsWithIgnoreCase(authorization, PREFIX)) {
                throw new BusinessException(ResponseTypes.PARAMETER_UNVALID);
            }
            String token = request.getHeader("Authorization").replace(PREFIX, "").trim();
            Claims claims = getClaimsFormToken(token);
            CurrentUserContext currentUserInfo = JSON.parseObject(claims.getSubject(), CurrentUserContext.class);
            return Optional.of(currentUserInfo.getRybm());
        }
    }
    private Claims getClaimsFormToken(String token) {
        return Jwts.parser()
                .setSigningKey(configuration.getAuthSecretKey()).parseClaimsJws(token)
                .getBody();
    }
}