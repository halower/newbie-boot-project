package com.newbie.core.datasource.jdbctemplate;

import org.springframework.jdbc.core.JdbcTemplate;


public interface JdbcTemplateManager {
     void init();
    JdbcTemplate get(String key);
}
