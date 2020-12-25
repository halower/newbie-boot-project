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

package com.newbie.context;

/**
 * @Author: halower
 * @Date: 2019/5/22 13:45
 *
 */
public class UserInfoManager {

    private ThreadLocal<CurrentUserContext> userInfoThreadLocal = new InheritableThreadLocal<>();

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
