package com.newbie.core.utils;

public class DependencyUtil {

    public static boolean hasDependency(String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
