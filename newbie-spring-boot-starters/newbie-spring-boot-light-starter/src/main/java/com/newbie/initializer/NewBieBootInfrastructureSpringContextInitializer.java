
package com.newbie.initializer;


import com.newbie.context.NewBieBootEnvUtil;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

public class NewBieBootInfrastructureSpringContextInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext>,
                                                           Ordered {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        if (NewBieBootEnvUtil.isSpringCloudBootstrapEnvironment(environment)) {
           System.out.println("\n当前接入SpringCloud环境");
        }

        System.out.println("\nNewBieBoot基础设施已经准备就绪!");
    }

    @Override
    public int getOrder() {
        //设置为最高优先级
        return HIGHEST_PRECEDENCE;
    }
}
