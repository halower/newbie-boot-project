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
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

/**
 * @Author: halower
 * @Date: 2019/5/22 14:06
 *
 */
@Configuration
@Component
public class DataSourceForDubboFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result;
        try {
            String write_read_db_type = RpcContext.getContext().getAttachment(NewbieBootInfraConstants.READ_WRITE_DB_TYPE);
            if(StringUtils.isEmpty(write_read_db_type)) {
                write_read_db_type = "DEFAULT";
            }
            if (RpcContext.getContext().isProviderSide()) {
                var legal_db_request = Arrays.asList(NewbieBootInfraConstants.LEGAL_DB_REQUEST.split(","));
                if (!legal_db_request.contains(write_read_db_type.toUpperCase())) {
                    throw new BusinessException("当前请求库不存在");
                }
                var databaseSourceKey = DatabaseSourceKey.valueOf(write_read_db_type.toUpperCase(Locale.ENGLISH));
                DynamicDataSourceContextHolder.setDataSourceType(databaseSourceKey);
            }
            result = invoker.invoke(invocation);
            return result;
        } finally {
            if (RpcContext.getContext().isProviderSide()) {
                DynamicDataSourceContextHolder.clearDataSourceType();
            }
        }
    }
}



