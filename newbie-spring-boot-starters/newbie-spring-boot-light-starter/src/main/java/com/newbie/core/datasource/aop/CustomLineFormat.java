package com.newbie.core.datasource.aop;

import com.newbie.context.NewBieBootEnvUtil;
import com.newbie.core.datasource.util.SQLFormatter;
import com.newbie.core.exception.BusinessException;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;


@Log4j2
public class CustomLineFormat implements MessageFormattingStrategy {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        Environment env = NewBieBootEnvUtil.getContext().getEnvironment();
        String traceEnabledStr = StringUtils.defaultIfEmpty(env.getProperty("application.tracker.enabled"), "false");
        Boolean traceEnabled = Boolean.parseBoolean(traceEnabledStr);
        if(!traceEnabled) {
            return StringUtils.EMPTY;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            Boolean isDDL = sql.matches(".*(update|UPDATE|insert|INSERT).*");
            if(isDDL) {
                Boolean isValidteSQL = sql.matches(".*(zhxgsj|ZHXGSJ|sjbsbh|SJBSBH|sjly|SJLY).*");
                if(!isValidteSQL) {
                    System.out.println("\033[41;38;1m" +"         警告: 当前为非查询操作缺失公共字段( zhxgsj|sjbsbh|sjly )的维护         "+"\033[0m");
                    String thorwExceptionStr = StringUtils.defaultIfEmpty(env.getProperty("application.tracker.thorw-exception"), "true");
                    Boolean thorwException = Boolean.parseBoolean(thorwExceptionStr);
                    if(thorwException) {
                        throw  new BusinessException("警告: 当前为非查询操作缺失公共字段( zhxgsj|sjbsbh|sjly )的维护");
                    }
                }
            }
            if (StringUtils.isNotEmpty(sql.trim())) {
                stringBuilder.append("\r\n当前时间:" +  now + "| 数据源:" + url.replaceAll(":p6spy","") + "\r\n");
                String runTime;
                String compareElapsedStr = StringUtils.defaultIfEmpty(env.getProperty("application.tracker.elapsed"), "50");
                Integer compareElapsed = Integer.parseInt(compareElapsedStr);
                if(elapsed <= compareElapsed ) {
                    runTime  = "执行耗费:" + elapsed + "毫秒 \33[34;42;1m (正常)" +"\33[0m";
                } else {
                    runTime =  "执行耗费:" + elapsed + "毫秒 \33[35;43;1m (慢SQL预警)" + "\33[0m";
                }
                stringBuilder.append("连接Id:"+connectionId+" | 语句分类:"+category + "|" + runTime + "\r\n");
                String sqlFormatStr = StringUtils.defaultIfEmpty(env.getProperty("application.tracker.sql-format"), "true");
                Boolean sqlFormat = Boolean.parseBoolean(sqlFormatStr);
                stringBuilder.append("SQL语句:" + (sqlFormat ? SQLFormatter.format(sql) : sql));
            }
            return stringBuilder.toString();
        }
    }
}