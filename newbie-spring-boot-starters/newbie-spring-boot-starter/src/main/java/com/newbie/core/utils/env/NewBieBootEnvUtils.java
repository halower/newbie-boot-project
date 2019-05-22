package com.newbie.core.utils.env;

import com.newbie.constants.NewbieBootInfraConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

public class NewBieBootEnvUtils {

    private final static String SPRING_CLOUD_MARK_NAME = "org.springframework.cloud.bootstrap.BootstrapConfiguration";

    private static ApplicationContext context ;
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
