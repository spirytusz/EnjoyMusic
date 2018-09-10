package com.zspirytus.enjoymusic.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCoverCache;
import com.zspirytus.enjoymusic.cache.MyApplication;
import com.zspirytus.enjoymusic.cache.finalvalue.FinalValue;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.StatusBarEventReceiver;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Notification帮助类，负责显示、更新Notification
 * Created by ZSpirytus on 2018/8/11.
 */

public class NotificationHelper {

    private static NotificationHelper INSTANCE;

    private static final int NOTIFICATION_MANAGER_NOTIFY_ID = 1;

    private static final NotificationManager notificationManager
            = (NotificationManager) MyApplication.getGlobalContext().getSystemService(NOTIFICATION_SERVICE);

    private static Notification mCurrentNotification;
    private static String channelId;
    private static RemoteViews mNotificationContentView;

    private NotificationHelper() {

    }

    public static NotificationHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NotificationHelper();
        }
        return INSTANCE;
    }

    public void showNotification(Music music) {
        if (channelId == null) {
            createNotificationChannel();
        }
        createNotification(music);
        notificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, mCurrentNotification);
    }

    public void updateNotificationClearable(boolean canClear) {
        if (canClear && mCurrentNotification != null) {
            mCurrentNotification.flags = Notification.FLAG_AUTO_CANCEL;
        } else {
            mCurrentNotification.flags = Notification.FLAG_NO_CLEAR;
        }
        notificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, mCurrentNotification);
    }

    public void setPlayOrPauseBtnRes(int resId) {
        mNotificationContentView.setImageViewResource(R.id.notification_music_play_pause, resId);
        notificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, mCurrentNotification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = "music_notification";
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel channel = new NotificationChannel(channelId, "音乐通知栏", importance);
            notificationManager.createNotificationChannel(channel);
        } else {
            channelId = "default";
        }
    }

    private void createNotification(Music music) {
        createNotificationView(music);
        mCurrentNotification = new NotificationCompat.Builder(MyApplication.getGlobalContext(), channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomContentView(mNotificationContentView)
                .build();
    }

    private void createNotificationView(Music music) {
        mNotificationContentView = new RemoteViews(MyApplication.getGlobalContext().getPackageName(), R.layout.notification_music_large);
        Bitmap cover = MusicCoverCache.getInstance().get(music.hashCode());
        if (cover != null) {
            mNotificationContentView.setImageViewBitmap(R.id.notification_music_cover, cover);
        } else {
            String coverUri = music.getMusicThumbAlbumCoverPath();
            if (coverUri != null) {
                Bitmap newCover = BitmapFactory.decodeFile(coverUri);
                MusicCoverCache.getInstance().put(music.hashCode(), newCover);
                mNotificationContentView.setImageViewBitmap(R.id.notification_music_cover, newCover);
            } else {
                // no cover, set default.
            }
        }
        String musicName = music.getMusicName();
        if (musicName != null) {
            mNotificationContentView.setTextViewText(R.id.notification_music_name, musicName);
        }
        String musicArtist = music.getMusicArtist();
        if (musicArtist != null) {
            mNotificationContentView.setTextViewText(R.id.notification_music_artist, musicArtist);
        }
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent = new Intent(MyApplication.getGlobalContext(), StatusBarEventReceiver.class);
        } else {
            intent = new Intent(FinalValue.StatusBarEvent.ACTION_NAME);
        }
        intent.putExtra(FinalValue.StatusBarEvent.EXTRA, FinalValue.StatusBarEvent.PREVIOUS);
        PendingIntent previousMusicPendingIntent = createPendingIntentByExtra(intent, 0, FinalValue.StatusBarEvent.EXTRA, FinalValue.StatusBarEvent.PREVIOUS);
        mNotificationContentView.setOnClickPendingIntent(R.id.notification_music_previous, previousMusicPendingIntent);

        PendingIntent playOrPauseMusicPendingIntent = createPendingIntentByExtra(intent, 1, FinalValue.StatusBarEvent.EXTRA, FinalValue.StatusBarEvent.PLAY_OR_PAUSE);
        mNotificationContentView.setOnClickPendingIntent(R.id.notification_music_play_pause, playOrPauseMusicPendingIntent);

        PendingIntent nextMusicPendingIntent = createPendingIntentByExtra(intent, 2, FinalValue.StatusBarEvent.EXTRA, FinalValue.StatusBarEvent.NEXT);
        mNotificationContentView.setOnClickPendingIntent(R.id.notification_music_next, nextMusicPendingIntent);

        PendingIntent singleClickPendingIntent = createPendingIntentByExtra(intent, 4, FinalValue.StatusBarEvent.EXTRA, FinalValue.StatusBarEvent.SINGLE_CLICK);
        mNotificationContentView.setOnClickPendingIntent(R.id.notification, singleClickPendingIntent);
    }

    private PendingIntent createPendingIntentByExtra(Intent intent, int requestCode, String extra, String value) {
        intent.putExtra(extra, value);
        return PendingIntent.getBroadcast(MyApplication.getGlobalContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
