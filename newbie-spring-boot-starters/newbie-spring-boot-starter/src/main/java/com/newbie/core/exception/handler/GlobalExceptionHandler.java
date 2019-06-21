package com.newbie.core.exception.handler;

import com.newbie.core.exception.BusinessException;
import com.newbie.core.exception.FileDownloadException;
import com.newbie.dto.ResponseResult;
import com.newbie.dto.ResponseTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @Author: 谢海龙
 * @Date: 2019/6/21 9:54
 * @Description
 */
@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult handleException(Exception e) {
        log.error("系统内部异常，异常信息", e);
        return new ResponseResult(ResponseTypes.UNKNOW,"系统内部异常");
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseResult handleParamsInvalidException(BusinessException e) {
        log.error("业务服务执行异常", e);
        return new ResponseResult(e.getExceptionType(),e.getMessage());
    }

    /**
     *  请求参数校验
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult validExceptionHandler(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new ResponseResult(ResponseTypes.PARAMETER_UNVALID,message.toString());
    }

    @ExceptionHandler(value = FileDownloadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleFileDownloadException(FileDownloadException e) {
        log.error("FileDownloadException", e);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseResult handleError(ResponseStatusException e) {
        log.error("响应状态异常:{}", e.getMessage());
        return new ResponseResult(ResponseTypes.UNKNOW,"响应状态异常");
    }
}
