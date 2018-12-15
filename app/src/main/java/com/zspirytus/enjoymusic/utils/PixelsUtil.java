package com.zspirytus.enjoymusic.utils;

import android.content.Context;

/**
 * Created by ZSpirytus on 2018/12/15.
 * <p>
 * Dp & Px互转工具类
 */

public class PixelsUtil {

    private PixelsUtil() {
        throw new AssertionError();
    }

    public static int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
