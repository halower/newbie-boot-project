package com.newbie.core.conventions;

import com.newbie.core.audit.CurrentUserInfo;
import com.newbie.core.audit.UserManager;

public interface ApplicationService {
     default CurrentUserInfo getCurrentUserInfo(){
         return UserManager.getUserInfo();
     }
}
