package com.zspirytus.enjoymusic.utils;

/**
 * Created by ZSpirytus on 2018/9/4.
 */

public class TimeUtil {

    public static String convertIntToMinsSec(int milli) {
        int seconds = 0;
        int minutes = 0;
        int totalSeconds = milli / 1000;
        minutes = totalSeconds / 60;
        if (totalSeconds > 60) {
            totalSeconds -= minutes * 60;
        }
        seconds = totalSeconds;
        String secondsStr;
        String minutesStr;
        if (seconds < 10) {
            secondsStr = "0" + seconds;
        } else {
            secondsStr = seconds + "";
        }
        if (minutes < 10) {
            minutesStr = "0" + minutes;
        } else {
            minutesStr = minutes + "";
        }
        return minutesStr + ":" + secondsStr;
    }
}
