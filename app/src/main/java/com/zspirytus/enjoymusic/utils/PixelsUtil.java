package com.zspirytus.enjoymusic.utils;

import android.content.Context;
import android.content.res.Resources;

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
     * Get NavBar Height.
     *
     * @param context context
     * @return NavBar Height
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }

}
