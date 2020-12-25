package com.newbie.core.datasource.jdbctemplate;

import com.newbie.context.NewbieBootContext;
import com.newbie.core.datasource.DynamicDataSourceRegister;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@ConditionalOnClass({ DataSourceAutoConfiguration.class })
public class DynamicJdbcTemplateManager implements JdbcTemplateManager, ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void init() {
        final Environment environment = applicationContext.getEnvironment();
        String datasourceList = environment.getProperty("application.datasource.list");
        String[] targetDatasource = StringUtils.isNotEmpty(datasourceList) ? datasourceList.split(",") : new String[]{"default"};
        final DynamicDataSourceRegister dynamicDataSourceRegister = new DynamicDataSourceRegister();
        Map<String, DataSource> map = new HashMap<>();
        for (String key : targetDatasource) {
            final DataSource dataSource = dynamicDataSourceRegister.buildDataSource(environment, key);
            map.put(StringUtils.upperCase(key), dataSource);
        }

        NewbieBootContext.setDataSourceMap(map);
    }

    @Override
    public JdbcTemplate get(String key) {
        final DataSource dataSource = NewbieBootContext.getDataSourceMap().get(key);
        return new JdbcTemplate(dataSource);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
