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

package com.newbie.core.exception.handler;

import com.alibaba.fastjson.JSON;
import com.newbie.core.aop.config.NewBieBasicConfiguration;
import com.newbie.core.exception.BusinessException;
import com.newbie.core.exception.FileDownloadException;
import com.newbie.core.exception.ResourceNotFoundException;
import com.newbie.dto.ResponseResult;
import com.newbie.dto.ResponseTypes;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: halower
 * @Date: 2019/6/21 9:54
 *
 */
@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler  {
    @Autowired
    NewBieBasicConfiguration  configuration;

    /**
     *  请求参数校验
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult bindExceptionHandler(BindException e) {
        if(configuration.getEnv().equals("dev")){
            e.printStackTrace();
        }
        log.error("参数绑定异常",  e);
        var message = getErrorMessages(e);
        return new ResponseResult(ResponseTypes.PARAMETER_UNVALID, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        if(configuration.getEnv().equals("dev")){
            e.printStackTrace();
        }
        log.error("参数验证异常", e);
        var message = getErrorMessages(e);
        return new ResponseResult(ResponseTypes.PARAMETER_UNVALID, message);
    }


    @ExceptionHandler(value = FileDownloadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult handleFileDownloadHandler(FileDownloadException e) {
        if(configuration.getEnv().equals("dev")){
            e.printStackTrace();
        }
        log.error("文件下载异常", e);
        return new ResponseResult(ResponseTypes.FILE_DOWN_FAIL,e.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ResponseResult responseStatusHandler(ResponseStatusException e) {
        if(configuration.getEnv().equals("dev")){
            e.printStackTrace();
        }
        log.error("响应状态异常:{}", e);
        return new ResponseResult(ResponseTypes.UNKNOW,e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseResult resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        if(configuration.getEnv().equals("dev")){
            e.printStackTrace();
        }
        log.error("未找到资源异常:{}", e);
        return new ResponseResult(ResponseTypes.FILE_DELETE_FAIL,e.getMessage());
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult handleParamsInvalidException(BusinessException e) {
        if(configuration.getEnv().equals("dev")){
            e.printStackTrace();
        }
        log.error("业务服务执行异常",  e);
        return new ResponseResult(e.getExceptionType(),e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseResult handleException(Exception e) {
        if(configuration.getEnv().equals("dev")){
            e.printStackTrace();
        }
        log.error("系统内部异常，异常信息", e);
        final String clazzName = "com.newbie.core.exception.BusinessException";
        if(e.getMessage().startsWith(clazzName)) {
            Matcher matcher = Pattern.compile("("+clazzName+"\\{.*\\})").matcher(e.getMessage());
            if(matcher.find()){
                var exStr = matcher.group(0).replace("\\","").substring(clazzName.length());
                var exception = JSON.parseObject(exStr, BusinessException.class);
                return new ResponseResult(exception.getExceptionType(),exception.getMessage());
            }
        }
        return new ResponseResult(ResponseTypes.UNKNOW,e.getMessage());
    }


    private String getErrorMessages(Exception e) {
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = null;
        if(e instanceof MethodArgumentNotValidException) {
            fieldErrors = ((MethodArgumentNotValidException)e).getBindingResult().getFieldErrors();
        }
        if(e instanceof BindException) {
            fieldErrors = ((BindException)e).getBindingResult().getFieldErrors();
        }
        for (FieldError error : fieldErrors) {
            msg.append(String.join(" - ",error.getDefaultMessage(), error.getField()));
        }
        return msg.toString();
    }

}
