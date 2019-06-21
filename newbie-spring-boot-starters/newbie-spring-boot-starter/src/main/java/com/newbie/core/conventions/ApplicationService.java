package com.newbie.core.conventions;

import com.newbie.core.audit.CurrentUserContext;
import com.newbie.context.UserInfoManager;

public interface ApplicationService {
     default CurrentUserContext getCurrentUserInfo(){
         return UserInfoManager.getInstance().getUserInfo();
     }
}
