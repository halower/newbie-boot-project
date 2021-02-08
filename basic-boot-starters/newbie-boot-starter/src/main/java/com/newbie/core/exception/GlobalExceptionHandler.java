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

package com.newbie.core.exception;

import com.newbie.common.api.JsonResult;
import com.newbie.core.config.NewbieBootBasicProperties;
import com.newbie.core.dubbo.validation.ValidateInfoBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.List;

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
    NewbieBootBasicProperties configuration;

    /**
     *  请求参数校验
     */
    @ExceptionHandler(BindException.class)
    public JsonResult bindExceptionHandler(BindException e) {
        log.error("",  e);
        return JsonResult.validateFailed(getErrorMessages(e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("", e);
        return JsonResult.validateFailed(getErrorMessages(e));
    }


    @ExceptionHandler(ResponseStatusException.class)
    public JsonResult responseStatusHandler(ResponseStatusException e) {
        if(configuration.getEnv().equals("dev")){
            e.printStackTrace();
        }
        log.error("", e);
        return  JsonResult.failed(e.getMessage());
    }



    @ExceptionHandler(value = ApiException.class)
    public JsonResult handleParamsInvalidException(ApiException e) {
        log.error("",  e);
        return  JsonResult.failed(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public JsonResult handleException(Exception e) {
        log.error("", e);
        return JsonResult.failed(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public JsonResult constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("",  e);
        var details = ValidateInfoBuilder.info(e);
        return JsonResult.failed(details.toString());
    }

    @ExceptionHandler(RpcException.class)
    public JsonResult rpcExceptionHandler(RpcException e) {
        log.error("",  e);
        return JsonResult.failed( e.getMessage());
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
            msg.append(String.join(",",error.getDefaultMessage()));
        }
        return msg.toString();
    }

}
