package com.newbie.core;

import com.newbie.core.util.id.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@Slf4j
public class RegexPatternDemo {

  public void test() {
    String sql = "insert into ws_xt_hssjy (sfsc, zhxgsj, ajlbbm, hsmc, sjylx, sjyywmc, sjyzwmc, syfw, tj) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    StringBuilder sb = new StringBuilder(sql);
    String date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
    String bjbh =  IdUtil.fastUUID().toString();
    String netId = "1"; //NewBieBootEnvUtil.getContext().getEnvironment().getProperty("application.network-id");
    sb.insert(sb.indexOf(")"), ", cjsj, zhxgsj, sjbsbh, sjly");
    sb.insert(sb.lastIndexOf(")"), String.format(", '%s', '%s', '%s', '%s'", date,date,bjbh,netId));
    System.out.println(sb.toString());
  }

  public void test2() {
    String sql="UPDATE xxxx XXX SET XXX";
    System.out.println(Pattern.matches(".*\\s+SET\\s+.*",sql));
  }

  @Test
  public void logic() {
    Integer DATABASE_URL_ENCRYPTED = 1;
    Integer DATABASE_TRACKERD = 2;
    Integer DATABASE_URL_ENCRYPTED_AND_DATABASE_TRACKERD = DATABASE_URL_ENCRYPTED | DATABASE_TRACKERD;
    Integer CURRENT_DATASOURCE_MODEL = 0;
    Boolean dataSourceTracked =true;
    Boolean dataSourceUrlEncrypted = false; //数据源是否加密
    if(dataSourceTracked) { //开启数据源监控
      CURRENT_DATASOURCE_MODEL|= DATABASE_TRACKERD;
      CURRENT_DATASOURCE_MODEL |= dataSourceUrlEncrypted ? CURRENT_DATASOURCE_MODEL | DATABASE_URL_ENCRYPTED : 0;
    } else {
      CURRENT_DATASOURCE_MODEL |= dataSourceUrlEncrypted ? CURRENT_DATASOURCE_MODEL | DATABASE_URL_ENCRYPTED : 0;
    }

    if(CURRENT_DATASOURCE_MODEL == DATABASE_TRACKERD) {
    }
    if(CURRENT_DATASOURCE_MODEL == DATABASE_URL_ENCRYPTED) {
    }

    if(CURRENT_DATASOURCE_MODEL == DATABASE_URL_ENCRYPTED_AND_DATABASE_TRACKERD) {

    }
    System.out.println(CURRENT_DATASOURCE_MODEL);
  }
}
