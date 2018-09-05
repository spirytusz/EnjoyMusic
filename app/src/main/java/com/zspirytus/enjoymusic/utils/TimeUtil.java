package com.zspirytus.enjoymusic.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ZSpirytus on 2018/9/4.
 */

public class TimeUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.CHINA);

    public static String convertIntToMinsSec(int milli) {
        return convertLongToMinsSec(milli);
    }

    public static String convertLongToMinsSec(long milli) {
        return simpleDateFormat.format(new Date(milli));
    }
}
