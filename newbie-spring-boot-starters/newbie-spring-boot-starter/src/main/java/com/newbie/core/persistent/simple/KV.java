package com.newbie.core.persistent.simple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: 谢海龙
 * @Date: 2019/4/17 19:50
 * @Description
 */
@Data
@Builder
public class KV {
  private String key;
  private Object value;
}
