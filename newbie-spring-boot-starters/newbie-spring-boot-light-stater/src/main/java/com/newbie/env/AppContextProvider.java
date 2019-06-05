package com.newbie.env;

import com.newbie.core.utils.env.NewBieBootEnvUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 谢海龙
 * @Date: 2019/4/25 19:57
 * @Description
 */
@Configuration
public class AppContextProvider implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        NewBieBootEnvUtils.setApplicationContext(applicationContext);
    }
}
