package com.zspirytus.enjoymusic.utils;

import android.content.Context;

import java.lang.reflect.Method;

public class StatusBarUtil {

    private StatusBarUtil() {
        throw new AssertionError();
    }

    public static void collapseStatusBar(Context context) {
        try {
            Object statusBarManager = context.getSystemService("statusbar");
            Method collapse;
            collapse = statusBarManager.getClass().getMethod("collapsePanels");
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
}
