package com.zspirytus.enjoymusic.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zspirytus.enjoymusic.services.NotificationHelper;

import java.util.Calendar;

/**
 * 定时任务
 * 使用方法:
 * 启动: object1.setAlarm(context, field, amount)
 * 取消: object2.cancel(context)
 * object1, object2可以相同，也可以不同
 */
public class MyAlarm extends BroadcastReceiver {

    private static final String TAG = "MyAlarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper.getInstance().cancel();
    }

    /**
     * 启动定时任务
     *
     * @param context context
     * @param field   单位
     * @param amount  值
     */
    public void setAlarm(Context context, int field, int amount) {
        /*
         * create new timer task.
         */
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(field, amount);
        am.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
    }

    /**
     * 取消定时任务
     *
     * @param context context
     */
    public void cancelAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        /*
         * create PendingIntent for canceling task
         */
        /*
         * 这里的Intent，只需要保证setAlarm中的intent1和这里的intent是相同的
         * 即intent.filterEqual(intent1) == true 即可
         * PendingIntent的requestCode无所谓
         */
        Intent intent = new Intent(context, MyAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        /*
         * cancel task.
         */
        am.cancel(pendingIntent);
    }
}
