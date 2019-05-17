package com.newbie.core.aop;

import com.newbie.core.aop.config.NewBieBasicConfiguration;
import com.newbie.core.exception.BusinessException;
import com.newbie.dto.ResponseResult;
import com.newbie.dto.ResponseTypes;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;



@Log
@ControllerAdvice
public class ErrorControllerAdvice  {
    @Autowired
    private NewBieBasicConfiguration basicConfig;

    @ExceptionHandler(value=Exception.class)
    @ResponseBody
    public ResponseResult allExceptionHandler(HttpServletRequest request, Exception exception) {
       var isDev = basicConfig.getEnv().equals("dev");
       if(isDev) { exception.printStackTrace(); }
        if(exception instanceof BusinessException){
            var exceptionType = ((BusinessException) exception).getExceptionType();
            return new ResponseResult(exceptionType,exception.getMessage());
        }
        return new ResponseResult(ResponseTypes.UNKNOW,exception.getMessage());
    }
}
