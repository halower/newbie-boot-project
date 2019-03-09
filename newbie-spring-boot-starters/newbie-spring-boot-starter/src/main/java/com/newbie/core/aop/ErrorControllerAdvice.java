package com.newbie.core.aop;

import com.newbie.core.aop.config.NewBieBasicConfig;
import com.newbie.core.dto.ResponseResult;
import com.newbie.core.exception.BusinessException;
import com.newbie.core.exception.ResponseTypes;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@ControllerAdvice
public class ErrorControllerAdvice  {
    @Autowired
    private NewBieBasicConfig basicConfig;

    @ExceptionHandler(value=Exception.class)
    @ResponseBody
    public ResponseResult allExceptionHandler(HttpServletRequest request, Exception exception) {
        if(exception instanceof BusinessException){
            var exceptionType = ((BusinessException) exception).getExceptionType();
            ResponseResult res = new ResponseResult(exceptionType,!basicConfig.getEnv().equals("dev")? exceptionType.getDesc() : exception.getMessage(),null);
            return res;
        }
        var res = new ResponseResult(ResponseTypes.UNKNOW,!basicConfig.getEnv().equals("dev")? ResponseTypes.UNKNOW.getDesc() : exception.getMessage(),null);
        return  res;

    }
}
