package com.zspirytus.enjoymusic.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by ZSpirytus on 2018/12/15.
 * <p>
 * 屏幕像素工具类
 */

public class PixelsUtil {

    private PixelsUtil() {
        throw new AssertionError();
    }

    public static int dp2px(Context context, int dp) {
        return dp2px(context, (float) dp);
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static float px2dp(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return px / scale + 0.5f;
    }

    /**
     * Get Screen Width & Height Pixels
     *
     * @return Screen Width & Height Pixels wrapped in array
     */
    public static int[] getPixelsConfig() {
        int[] pixelsConfig = new int[2];
        pixelsConfig[0] = Resources.getSystem().getDisplayMetrics().widthPixels;
        pixelsConfig[1] = Resources.getSystem().getDisplayMetrics().heightPixels;
        return pixelsConfig;
    }

    /**
     * Get NavigationBar Height
     *
     * @param context
     * @return NavigationBar Height
     */
    public static int getVirtualBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

}
