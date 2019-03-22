package com.zspirytus.enjoymusic.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import java.lang.ref.WeakReference;
import java.util.Calendar;

public class MyAlarm extends BroadcastReceiver {

    private static class Singleton {
        static MyAlarm INSTANCE = new MyAlarm();
    }

    private static final String TAG = "MyAlarm";

    private static final String ALARM_NAME_KEY = "alarmName";

    private SparseArray<WeakReference<PendingIntent>> mPendingIntentCache;
    private SparseArray<Runnable> mTaskCache;

    public MyAlarm() {
        mPendingIntentCache = new SparseArray<>();
        mTaskCache = new SparseArray<>();
    }

    public static MyAlarm getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmName = intent.getStringExtra(ALARM_NAME_KEY);
        Runnable task = mTaskCache.get(alarmName.hashCode());
        if (task != null) {
            task.run();
        }
    }

    public void setAlarm(Context context, String alarmName, int field, int amount, Runnable runnable) {
        /*
         * create new timer task.
         */
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, MyAlarm.class);
        i.putExtra(ALARM_NAME_KEY, alarmName);
        PendingIntent pi = PendingIntent.getBroadcast(context, alarmName.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(field, amount);
        am.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pi);

        /*
         * cache PendingIntent for reusing.
         * cache Runnable for delay execute.
         */
        WeakReference<PendingIntent> weakReference = new WeakReference<>(pi);
        mPendingIntentCache.put(alarmName.hashCode(), weakReference);
        mTaskCache.put(alarmName.hashCode(), runnable);
    }

    public void cancelAlarm(Context context, String alarmName) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        /*
         * obtain pendingIntent from cache.
         * if it is null, create new one.
         */
        WeakReference<PendingIntent> weakReference = mPendingIntentCache.get(alarmName.hashCode());
        if (weakReference == null) {
            return;
        }
        PendingIntent pendingIntent = weakReference.get();
        if (pendingIntent == null) {
            Intent intent = new Intent(context, MyAlarm.class);
            pendingIntent = PendingIntent.getBroadcast(context, alarmName.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        /*
         * delete cache.
         */
        mPendingIntentCache.remove(alarmName.hashCode());
        mTaskCache.remove(alarmName.hashCode());
        am.cancel(pendingIntent);
    }
}
