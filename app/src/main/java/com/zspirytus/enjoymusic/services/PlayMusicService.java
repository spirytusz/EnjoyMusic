package com.zspirytus.enjoymusic.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.model.Music;
import com.zspirytus.enjoymusic.view.activity.MainActivity;

import java.io.IOException;

public class PlayMusicService extends Service {

    private static final String TAG = "PlayMusicService";

    private MyBinder binder = new MyBinder();

    public PlayMusicService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showNotificationOnO(null);
        } else {
            showNotificationBelowO(null);
        }
        return binder;
    }

    public class MyBinder extends Binder {

        private MediaPlayHelper mediaPlayHelper = MediaPlayHelper.getInstance();

        public void play(Music music) {
            try {
                mediaPlayHelper.play(music);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void pause() {
            mediaPlayHelper.pause();
        }

        public void stop() {
            mediaPlayHelper.stop();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void showNotificationOnO(Music music) {
        String channelId = "music_notification";
        String channelName = "音乐通知栏";
        int importance = NotificationManager.IMPORTANCE_MIN;
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        RemoteViews notificationContentView = new RemoteViews(getPackageName(), R.layout.notification_music_large);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomContentView(notificationContentView)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    private void showNotificationBelowO(Music music) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        RemoteViews notificationContentView = new RemoteViews(getPackageName(), R.layout.notification_music_large);
        Notification notification = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContent(notificationContentView)
                .build();
        startForeground(1, notification);
    }
}
