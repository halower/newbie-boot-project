package com.newbie.core.datasource.aop;

import com.newbie.core.datasource.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Binder {
    public static void bindChainParameters(String WRITE_READ_DB_TYPE) {
      DynamicDataSourceContextHolder.setDataSourceType(WRITE_READ_DB_TYPE);
    }
}
