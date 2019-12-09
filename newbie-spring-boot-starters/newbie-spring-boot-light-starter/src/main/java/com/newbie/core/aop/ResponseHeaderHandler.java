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

//package com.newbie.core.aop;

//import com.newbie.core.aop.config.NewBieBasicConfiguration;
//import lombok.var;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Author: halower
 * @Date: 2019/7/5 14:01
 *配合apm的需求，当前废弃
 */
//@Configuration
//@RestControllerAdvice
//public class ResponseHeaderHandler implements ResponseBodyAdvice {
//
//    @Autowired
//    private NewBieBasicConfiguration basicConfig;
//
//
//    @Override
//    public boolean supports(MethodParameter returnType, Class converterType) {
//        String methodName=returnType.getMethod().getName();
//        return !basicConfig.getApmExcludeMethods().contains(methodName) && !methodName.endsWith("Exception");
//    }
//
//    @Override
//    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//        var  apmProperties = basicConfig.getApmProperties().iterator();
//        while (apmProperties.hasNext()) {
//            var willReturnKey =   apmProperties.next();
//            var willReturnValue = request.getHeaders().get(willReturnKey);
//            if(willReturnValue!=null) {
//                response.getHeaders().set(willReturnKey, willReturnValue.get(0));
//            }
//        }
//        return body;
//    }
//}
//
