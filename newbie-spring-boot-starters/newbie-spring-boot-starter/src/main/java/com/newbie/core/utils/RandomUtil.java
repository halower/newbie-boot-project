package com.newbie.core.utils;

import java.util.UUID;

/**
 * @Author: 谢海龙
 * @Date: 2019/7/20 10:28
 * @Description
 */
public class RandomUtil {
    public String getUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public static String simpleKey() {
        return String.valueOf(System.nanoTime());
    }
}