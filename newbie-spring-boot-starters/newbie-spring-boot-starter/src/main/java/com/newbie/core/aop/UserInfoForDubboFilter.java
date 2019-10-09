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

package com.newbie.core.aop;

import com.alibaba.fastjson.JSON;
import com.newbie.constants.NewbieBootInfraConstants;
import com.newbie.context.CurrentUserContext;
import com.newbie.context.UserInfoManager;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author: halower
 * @date: 2019/5/22 14:06
 *
 */
@Configuration
@Component
public class UserInfoForDubboFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result;
       try{
           String userInfo = RpcContext.getContext().getAttachment(NewbieBootInfraConstants.CURRENT_USER_INFO);
           if (StringUtils.isNotBlank(userInfo)) {
               var currentUserInfo = JSON.parseObject(userInfo, CurrentUserContext.class);
               UserInfoManager.getInstance().bind(currentUserInfo);
           }
            result = invoker.invoke(invocation);
            return result;
       }
       finally {
           UserInfoManager.getInstance().remove();
       }
    }
}



