package com.zspirytus.basesdk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ZSpirytus on 2018/9/4.
 */

public class TimeUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.CHINA);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd hh:mm:ss", Locale.CHINA);

    public static String timestamp2Time(long milli) {
        return simpleDateFormat.format(new Date(milli));
    }

    public static String getNowDateTime() {
        return sdf.format(new Date());
    }
}
