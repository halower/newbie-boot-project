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
import com.newbie.core.datasource.DynamicDataSourceContextHolder;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: halower
 * @Date: 2019/5/22 13:40
 */
@ConditionalOnClass({DataSourceAutoConfiguration.class})
public class DataSourceForWebFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String WRITE_READ_DB_TYPE = httpRequest.getHeader(NewbieBootInfraConstants.WRITE_READ_TYPE_FLAG);
        Binder.bindChainParameters(WRITE_READ_DB_TYPE);
        try {
            RpcContext.getContext().setAttachment(NewbieBootInfraConstants.READ_WRITE_DB_TYPE, WRITE_READ_DB_TYPE);
            chain.doFilter(request,response);
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }
}