package com.newbie.core.persistent.config;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: 谢海龙
 * @Date: 2019/4/17 19:50
 * @Description
 */
@Data
@Builder
public class FieldInfo {
  private String name;
  private Object value;
  private String type;
}
