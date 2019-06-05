package com.newbie.core.utils.env;

import com.newbie.core.audit.CurrentUserContext;

/**
 * @Author: 谢海龙
 * @Date: 2019/5/22 13:45
 * @Description
 */
public class UserInfoManager {

    private ThreadLocal<CurrentUserContext> userInfoThreadLocal = new ThreadLocal<>();

    private static UserInfoManager ourInstance = new UserInfoManager();

    public static UserInfoManager getInstance() {
        return ourInstance;
    }

    private UserInfoManager() {
    }

    /**
     * 将用户对象绑定到当前线程中，键为userInfoThreadLocal对象，值为userInfo对象
     *
     * @param userInfo
     */
    public void bind(CurrentUserContext userInfo) {
        userInfoThreadLocal.set(userInfo);
    }

    /**
     * 得到绑定的用户对象
     *
     * @return
     */
    public CurrentUserContext getUserInfo() {
        CurrentUserContext userInfo = userInfoThreadLocal.get();
        return userInfo;
    }
    /**
    * 移除绑定的用户对象
    */
    public void remove() {
        userInfoThreadLocal.remove();
    }
}
