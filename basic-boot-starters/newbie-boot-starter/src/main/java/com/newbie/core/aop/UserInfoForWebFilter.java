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
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: halower
 * @Date: 2019/5/22 13:40
 * @Update Date: 2019.12.18
 * @UpdateDesc: 修改原有JWT TOKEN逻辑到简单获取用户信息
 */
@Slf4j
public class UserInfoForWebFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String identityHeader = "X-IDENTITY";
        final String bmsahHeader = "X-BMSAH";
        String identity = httpRequest.getHeader(identityHeader);
        String bmsah = httpRequest.getHeader(bmsahHeader);
        // 备注目前生产环境也没有修改这个变量，因此dev不一定就是开发环境
        // 通过 "X-IDENTITY"来过滤即可

        try{
            if (StringUtil.isNullOrEmpty(identity)) {
                bindFormDefaultValue(bmsah);
                chain.doFilter(request, response);
            } else {
                // client object -> JSON.stringify -> base64
                // server base64 ---> string ----> object
                String originIdentityStr= StringUtils.newStringUtf8(Base64.decodeBase64(identity));
                CurrentUserContext userContext = JSON.parseObject(originIdentityStr, CurrentUserContext.class);
                userContext.setBmsah(bmsah);
                UserInfoManager.getInstance().bind(userContext);
                RpcContext.getContext().setAttachment(NewbieBootInfraConstants.CURRENT_USER_INFO, originIdentityStr);
                chain.doFilter(request, response);
            }
        }finally {
            UserInfoManager.getInstance().remove();
        }
    }

    private void bindFormDefaultValue(String bmsah) {
        CurrentUserContext userContext = CurrentUserContext.builder()
                .dlbm("本地模式调试")
                .rybm("0000000001")
                .dwmc("控制台")
                .bmsah(bmsah)
                .dwbm("000001").build();
        UserInfoManager.getInstance().bind(userContext);
        RpcContext.getContext().setAttachment(NewbieBootInfraConstants.CURRENT_USER_INFO, JSON.toJSONString(userContext));
    }
}


