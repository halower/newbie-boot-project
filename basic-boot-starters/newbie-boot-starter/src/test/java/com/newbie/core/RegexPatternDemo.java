package com.newbie.core;

import com.newbie.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@Slf4j
public class RegexPatternDemo {

  public void test() {
    String sql = "insert into ws_xt_hssjy (sfsc, zhxgsj, ajlbbm, hsmc, sjylx, sjyywmc, sjyzwmc, syfw, tj) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    StringBuilder sb = new StringBuilder(sql);
    String date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
    String bjbh = RandomUtil.getUUID();
    String netId = "1"; //NewBieBootEnvUtil.getContext().getEnvironment().getProperty("application.network-id");
    sb.insert(sb.indexOf(")"), ", cjsj, zhxgsj, sjbsbh, sjly");
    sb.insert(sb.lastIndexOf(")"), String.format(", '%s', '%s', '%s', '%s'", date,date,bjbh,netId));
    System.out.println(sb.toString());
  }

  public void test2() {
    String sql="UPDATE xxxx XXX SET XXX";
    System.out.println(Pattern.matches(".*\\s+SET\\s+.*",sql));
  }
}
