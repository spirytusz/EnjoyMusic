package com.zspirytus.enjoymusic.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCoverCache;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.view.activity.BaseActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Notification帮助类，负责显示Notification
 * Created by ZSpirytus on 2018/8/11.
 */

public class NotificationHelper {

    private static final Context GLOBAL_CONTEXT = BaseActivity.getContext();
    private static final int NOTIFICATION_MANAGER_NOTIFY_ID = 1;

    private static final String TAG = "PlayMusicService";
    private static final String DEFAULT_CHANNEL_ID = "default";
    private static final String MUSIC_NOTIFICATION_CHANNEL_ID = "music_notification";
    private static final String MUSIC_NOTIFICATION_CHANNEL_NAME = "音乐通知栏";

    private static final NotificationManager notificationManager
            = (NotificationManager) GLOBAL_CONTEXT.getSystemService(NOTIFICATION_SERVICE);

    private static Notification currentNotification;
    private static String channelId;

    public static void showNotification(Music music) {
        createNotificationChannel();

        createNotification(music);
        notificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, currentNotification);
    }

    public static void updateNotificationClearable(boolean canClear) {
        if (canClear) {
            currentNotification.flags = Notification.FLAG_AUTO_CANCEL;
        } else {
            currentNotification.flags = Notification.FLAG_NO_CLEAR;
        }
        notificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, currentNotification);
    }

    private static void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = MUSIC_NOTIFICATION_CHANNEL_ID;
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel channel = new NotificationChannel(channelId, MUSIC_NOTIFICATION_CHANNEL_NAME, importance);
            notificationManager.createNotificationChannel(channel);
        } else {
            channelId = DEFAULT_CHANNEL_ID;
        }
    }

    private static void createNotification(Music music) {
        currentNotification = new NotificationCompat.Builder(GLOBAL_CONTEXT, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomContentView(getNotificationView(music))
                .build();
    }

    private static RemoteViews getNotificationView(Music music) {
        RemoteViews notificationContentView = new RemoteViews(GLOBAL_CONTEXT.getPackageName(), R.layout.notification_music_large);
        Bitmap cover = MusicCoverCache.getInstance().get(music.hashCode());
        if (cover != null) {
            notificationContentView.setImageViewBitmap(R.id.notification_music_cover, cover);
        } else {
            String coverUri = music.getmMusicThumbAlbumUri();
            if (coverUri != null) {
                Bitmap newCover = BitmapFactory.decodeFile(coverUri);
                MusicCoverCache.getInstance().put(music.hashCode(), newCover);
                notificationContentView.setImageViewBitmap(R.id.notification_music_cover, newCover);
            } else {
                // no cover, set default.
            }
        }

        String musicName = music.getmMusicName();
        if (musicName != null) {
            notificationContentView.setTextViewText(R.id.notification_music_name, musicName);
        }

        String musicArtist = music.getmMusicArtist();
        if (musicArtist != null) {
            notificationContentView.setTextViewText(R.id.notification_music_artist, musicArtist);
        }
        return notificationContentView;
    }

}
