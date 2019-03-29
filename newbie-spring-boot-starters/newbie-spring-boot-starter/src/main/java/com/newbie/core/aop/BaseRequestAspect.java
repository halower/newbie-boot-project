package com.newbie.core.aop;

import com.alibaba.fastjson.JSON;
import com.newbie.core.aop.config.NewBieBasicConfiguration;
import com.newbie.core.audit.BusinessEventData;
import com.newbie.core.event.EventBus;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.var;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Log
@Aspect
@Component
public class BaseRequestAspect {
    @Autowired
    private NewBieBasicConfiguration basicConfig;

    @Pointcut(value ="execution(public * *..controller..*.*(..))")
    public void logPointCut() {
    }

    @SneakyThrows
    @Around("logPointCut()")
    public Object onProcess(ProceedingJoinPoint joinPoint) {
        var startTime = System.currentTimeMillis();
        Object next = joinPoint.proceed();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String className = joinPoint.getTarget().getClass().getName();
        Class<?> targetClass = Class.forName(className);
        String methodName = joinPoint.getSignature().getName();
        Method[] methods = targetClass.getMethods();
        Object[]  args = joinPoint.getArgs();
        BusinessEventData businessEventData;
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                Class<?>[] clazz = method.getParameterTypes();
                if (clazz.length == args.length) {
                    var desc = method.getAnnotation(ApiOperation.class);
                    businessEventData = BusinessEventData.builder().requestUrl(request.getRequestURL().toString())
                            .requestIp(request.getRemoteAddr()).requestMethod(request.getMethod())
                            .callMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + methodName)
                            .args(JSON.toJSONString(args))
                            .retrunVal(JSON.toJSONString(next))
                            .offsetTime((System.currentTimeMillis() - startTime) + "")
                            .build();
                    businessEventData.setDescription(desc!= null? desc.value(): "");
                    if(basicConfig.isReceiveRequestEvent()) {
                        EventBus.trigger(businessEventData);
                    }
                    if(basicConfig.isAutoRecordRequestDetails()){
                        log.info(JSON.toJSONString(businessEventData));
                    }
                    break;
                }
            }
        }
        return next;
    }
}
