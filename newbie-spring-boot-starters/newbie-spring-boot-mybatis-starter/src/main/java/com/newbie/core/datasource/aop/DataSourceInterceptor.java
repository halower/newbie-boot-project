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
//package com.newbie.core.datasource.aop;
//
//import com.newbie.constants.NewbieBootInfraConstants;
//import com.newbie.core.datasource.DatabaseSourceKey;
//import com.newbie.core.datasource.DynamicDataSourceContextHolder;
//import com.newbie.core.exception.BusinessException;
//import lombok.extern.log4j.Log4j2;
//import lombok.var;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Arrays;
//import java.util.Locale;
//
///**
// * @Author: halower
// * @Date: 2019/8/2 9:24
// * @Description
// */
//@Log4j2
//@Component
//@Configuration
//public class DataSourceInterceptor extends HandlerInterceptorAdapter {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        var wrf = NewbieBootInfraConstants.WRITE_READ_TYPE_FLAG;
//        DatabaseSourceKey databaseSourceKey;
//        if(request.getHeader(wrf)!=null && !request.getHeader(wrf).equals("")) {
//            String wrfName = request.getHeader(wrf).trim();
//            var legal_db_request =  Arrays.asList(NewbieBootInfraConstants.LEGAL_DB_REQUEST.split(","));
//            if(!legal_db_request.contains(wrfName.toUpperCase())) {
//                throw new BusinessException("当前请求库不存在");
//            }
//            databaseSourceKey =DatabaseSourceKey.valueOf(wrfName.toUpperCase(Locale.ENGLISH));
//            DynamicDataSourceContextHolder.setDataSourceType(databaseSourceKey);
//        } else {
//            DynamicDataSourceContextHolder.setDataSourceType(DatabaseSourceKey.DEFAULT);
//        }
//        return super.preHandle(request, response, handler);
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        DynamicDataSourceContextHolder.clearDataSourceType();
//        super.afterCompletion(request, response, handler, ex);
//    }
//}
