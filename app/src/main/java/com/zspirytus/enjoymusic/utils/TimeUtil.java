package com.zspirytus.enjoymusic.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ZSpirytus on 2018/9/4.
 */

public class TimeUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.CHINA);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd", Locale.CHINA);

    public static String convertIntToMinsSec(int milli) {
        return convertLongToMinsSec(milli);
    }

    public static String convertLongToMinsSec(long milli) {
        return simpleDateFormat.format(new Date(milli));
    }

    public static String getNowDate() {
        return sdf.format(new Date());
    }
}
