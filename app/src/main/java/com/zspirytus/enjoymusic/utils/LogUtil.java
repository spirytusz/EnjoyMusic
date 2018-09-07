package com.zspirytus.enjoymusic.utils;

import android.util.Log;

/**
 * 日志工具类
 * Created by ZSpirytus on 2018/8/11.
 */

public class LogUtil {

    private LogUtil() {
        throw new AssertionError("must not get class: " + this.getClass().getSimpleName() + " Instance!");
    }

    private static final int DEBUG = 1;
    private static final int RELEASE = 2;
    private static final int LEVEL = DEBUG;

    public static void e(String TAG, String message) {
        if (LEVEL == DEBUG) {
            Log.e(TAG, message);
        }
    }

    public static void out(Object message) {
        if (LEVEL == DEBUG) {
            System.out.println(message);
        }
    }

}
