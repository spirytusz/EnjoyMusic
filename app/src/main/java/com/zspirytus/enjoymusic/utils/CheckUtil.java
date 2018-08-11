package com.zspirytus.enjoymusic.utils;

/**
 * Created by ZSpirytus on 2018/8/10.
 */

public class CheckUtil {

    private CheckUtil() {
    }

    public static boolean isObjectsNotNull(Object... objects) {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                return false;
            }
        }
        return true;
    }
}
