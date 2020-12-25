package com.newbie.core.datasource.aop;

import com.newbie.core.datasource.DynamicDataSourceContextHolder;
import com.newbie.core.datasource.annotation.DS;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@Aspect
@ConditionalOnClass({ DataSourceAutoConfiguration.class })
public class DynamicDataSourceAspect {

    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, DS ds) {
        String dsId = ds.value();
        if (DynamicDataSourceContextHolder.databaseSourceKeys.contains(StringUtils.upperCase(dsId))) {
            DynamicDataSourceContextHolder.setDataSourceType(StringUtils.upperCase(dsId));
        } else {
            DynamicDataSourceContextHolder.setDataSourceType("DEFAULT");
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, DS ds) {
        DynamicDataSourceContextHolder.clearDataSourceType();

    }
}