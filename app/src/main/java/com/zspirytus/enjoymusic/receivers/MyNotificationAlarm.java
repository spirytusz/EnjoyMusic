package com.zspirytus.enjoymusic.receivers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zspirytus.enjoymusic.utils.LogUtil;

import java.util.Calendar;

public class MyNotificationAlarm extends BroadcastReceiver {

    private static final int NOTIFICATION_MANAGER_NOTIFY_ID = 233;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_MANAGER_NOTIFY_ID);
        LogUtil.e(this.getClass().getSimpleName(), "wake!");
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, MyNotificationAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.MINUTE, 3);
        am.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pi);
        LogUtil.e(this.getClass().getSimpleName(), "setAlarm!");
    }

    public void cancelAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, MyNotificationAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.cancel(pi);
    }
}
