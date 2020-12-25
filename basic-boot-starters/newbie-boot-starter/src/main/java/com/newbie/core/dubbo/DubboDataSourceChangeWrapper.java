package com.newbie.core.dubbo;


import com.alibaba.fastjson.JSON;
import com.newbie.context.CurrentUserContext;
import com.newbie.context.UserInfoManager;
import com.newbie.core.datasource.DynamicDataSourceContextHolder;
import com.newbie.core.datasource.aop.Binder;
import lombok.var;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;

import java.util.Map;

import static com.newbie.constants.NewbieBootInfraConstants.CURRENT_USER_INFO;
import static com.newbie.constants.NewbieBootInfraConstants.READ_WRITE_DB_TYPE;

@SPI
public class DubboDataSourceChangeWrapper implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Class<?> serviceType = invoker.getInterface();
        Map<String, Object> attachments = invocation.getObjectAttachments();
        String currentDB = (attachments == null ? null : (String) attachments.get(READ_WRITE_DB_TYPE));
        String currentUser = (attachments == null ? null : (String) attachments.get(CURRENT_USER_INFO));
        if (StringUtils.isEmpty(currentDB)) {
            throw new RpcException("Invalid Database! Forbid invoke remote service " + serviceType + " method " + invocation.getMethodName() + "() from consumer " + RpcContext.getContext().getRemoteHost() + " to provider " + RpcContext.getContext().getLocalHost());
        }
        if (StringUtils.isNotEmpty(currentUser)) {
            var currentUserInfo = JSON.parseObject(currentUser, CurrentUserContext.class);
            UserInfoManager.getInstance().bind(currentUserInfo);
        }
        try {
            if (RpcContext.getContext().isProviderSide()) {
                Binder.bindChainParameters(currentDB);
            }
            return invoker.invoke(invocation);
        } finally {
            if (RpcContext.getContext().isProviderSide()) {
                DynamicDataSourceContextHolder.clearDataSourceType();
            }
            UserInfoManager.getInstance().remove();
        }
    }
}
