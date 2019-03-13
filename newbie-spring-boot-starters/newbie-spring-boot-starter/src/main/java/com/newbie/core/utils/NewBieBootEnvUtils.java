package com.newbie.core.utils;

import com.newbie.constants.NewbieBootInfraConstants;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

public class NewBieBootEnvUtils {

    private final static String SPRING_CLOUD_MARK_NAME = "org.springframework.cloud.bootstrap.BootstrapConfiguration";

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
}
