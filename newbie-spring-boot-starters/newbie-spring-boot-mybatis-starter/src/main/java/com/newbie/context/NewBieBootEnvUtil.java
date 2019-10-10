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

import com.newbie.constants.NewbieBootInfraConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

public class NewBieBootEnvUtil {

    private final static String SPRING_CLOUD_MARK_NAME = "org.springframework.cloud.bootstrap.BootstrapConfiguration";

    private static ApplicationContext context ;

    public static String getNetworkFalg() {
        return networkFalg;
    }

    public static void setNetworkFalg(String networkFalg) {
        NewBieBootEnvUtil.networkFalg = networkFalg;
    }

    /*业务字段*/
    private static String networkFalg;

    public static boolean isSpringCloudBootstrapEnvironment(Environment environment) {
        if (environment instanceof ConfigurableEnvironment) {
            return !((ConfigurableEnvironment) environment).getPropertySources().contains(
                    NewbieBootInfraConstants.NEWBIE_BOOTSTRAP)
                    && isSpringCloud();
        }
        return false;
    }

    public static boolean isSpringCloud() {
        return ClassUtils.isPresent(SPRING_CLOUD_MARK_NAME, null);
    }

    public static void setApplicationContext(ApplicationContext applicationContext){
        context = applicationContext;
    }

    public static ApplicationContext getContext(){
        return context;
    }

    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> t){
        return context.getBean(t);
    }


}
