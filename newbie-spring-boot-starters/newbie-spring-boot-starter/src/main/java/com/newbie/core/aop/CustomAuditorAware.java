package com.newbie.core.aop;

import com.newbie.core.aop.config.NewBieBasicConfiguration;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class CustomAuditorAware implements AuditorAware<String> {
    @Autowired
    private NewBieBasicConfiguration basicConfig;

    @Override
    @SneakyThrows
    public Optional<String> getCurrentAuditor() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (basicConfig.getEnv().equals("dev") && !request.getMethod().equals("GET")) {
            if (request.getHeader(basicConfig.getAuditorTagInHead()) == null)
                throw new IllegalAccessException("参数"+basicConfig.getAuditorTagInHead()+"丢失");
            return Optional.of(request.getHeader(basicConfig.getAuditorTagInHead()));
        }
        return Optional.of("developer");
    }
}