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
package com.newbie.core.datasource.aop;

import com.newbie.constants.NewbieBootInfraConstants;
import com.newbie.core.datasource.DatabaseSourceKey;
import com.newbie.core.datasource.DynamicDataSourceContextHolder;
import com.newbie.core.exception.BusinessException;
import lombok.var;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

@Component
@Configuration
public class DataSourceForWebFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        var wrf = NewbieBootInfraConstants.WRITE_READ_TYPE_FLAG;
        DatabaseSourceKey databaseSourceKey;
        String write_read_db_type = "";
        if (httpRequest.getHeader(wrf) != null && !httpRequest.getHeader(wrf).equals("")) {
            write_read_db_type = httpRequest.getHeader(wrf).trim();
            var legal_db_request = Arrays.asList(NewbieBootInfraConstants.LEGAL_DB_REQUEST.split(","));
            if (!legal_db_request.contains(write_read_db_type.toUpperCase())) {
                throw new BusinessException("当前请求库不存在");
            }
            databaseSourceKey = DatabaseSourceKey.valueOf(write_read_db_type.toUpperCase(Locale.ENGLISH));
            DynamicDataSourceContextHolder.setDataSourceType(databaseSourceKey);
        } else {
            DynamicDataSourceContextHolder.setDataSourceType(DatabaseSourceKey.DEFAULT);
        }
        try {
            RpcContext.getContext().setAttachment(NewbieBootInfraConstants.READ_WRITE_DB_TYPE, write_read_db_type);
            chain.doFilter(request,response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }
}