package com.newbie.core.util;

import java.util.UUID;

public class RandomUtil {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }
    public static String simpleKey() {
        return String.valueOf(System.nanoTime());
    }
}