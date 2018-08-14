package com.zspirytus.enjoymusic.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.view.activity.BaseActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Notification帮助类，负责显示Notification
 * Created by ZSpirytus on 2018/8/11.
 */

public class NotificationHelper {

    private static final Context GLOBAL_CONTEXT = BaseActivity.getContext();

    private static final String TAG = "PlayMusicService";
    private static final String DEFAULT_CHANNEL_ID = "default";
    private static final String MUSIC_NOTIFICATION_CHANNEL_ID = "music_notification";
    private static final String MUSIC_NOTIFICATION_CHANNEL_NAME = "音乐通知栏";

    public static void showNotificationOnO(Music music, boolean canClear) {
        String channelId;
        NotificationManager notificationManager = (NotificationManager) GLOBAL_CONTEXT.getSystemService(
                NOTIFICATION_SERVICE);
        RemoteViews notificationContentView = new RemoteViews(GLOBAL_CONTEXT.getPackageName(), R.layout.notification_music_large);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            channelId = MUSIC_NOTIFICATION_CHANNEL_ID;
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel channel = new NotificationChannel(channelId, MUSIC_NOTIFICATION_CHANNEL_NAME, importance);
            notificationManager.createNotificationChannel(channel);
        } else {
            channelId = DEFAULT_CHANNEL_ID;
        }
        Notification notification = new NotificationCompat.Builder(GLOBAL_CONTEXT, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomContentView(notificationContentView)
                .build();
        if (canClear) {
            notification.flags = Notification.FLAG_AUTO_CANCEL;
        } else {
            notification.flags = Notification.FLAG_NO_CLEAR;
        }
        notificationManager.notify(1, notification);
    }

}
