package com.zspirytus.enjoymusic.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.cache.MyApplication;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Notification帮助类，负责显示、更新Notification
 * Created by ZSpirytus on 2018/8/11.
 */

public class NotificationHelper {

    private static class SingletonHolder {
        static NotificationHelper INSTANCE = new NotificationHelper();
    }

    private static final int NOTIFICATION_MANAGER_NOTIFY_ID = 1;

    private static final NotificationManager mNotificationManager
            = (NotificationManager) MyApplication.getBackgroundContext().getSystemService(NOTIFICATION_SERVICE);

    private static Notification mCurrentNotification;
    private static String mChannelId;
    private static RemoteViews mNotificationContentView;

    private NotificationHelper() {
    }

    public static NotificationHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void showNotification(Music music) {
        if (mChannelId == null) {
            createNotificationChannel();
        }
        if (mNotificationContentView == null) {
            createNotificationView(music);
            createNotification();
        } else {
            setupRemoteViews(music);
        }
        mNotificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, mCurrentNotification);
    }

    public void setPlayOrPauseBtnRes(boolean isPlaying) {
        if (mNotificationContentView != null) {
            int resId = isPlaying ? R.drawable.ic_pause_black_48dp : R.drawable.ic_play_arrow_black_48dp;
            mNotificationContentView.setImageViewResource(R.id.notification_music_play_pause, resId);
            mNotificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, mCurrentNotification);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannelId = "music_notification";
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel(mChannelId, "音乐通知栏", importance);
            mNotificationManager.createNotificationChannel(channel);
        } else {
            mChannelId = "default";
        }
    }

    private void createNotification() {
        mCurrentNotification = new NotificationCompat.Builder(MyApplication.getBackgroundContext(), mChannelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomContentView(mNotificationContentView)
                .setOngoing(true)
                .build();
    }

    private void createNotificationView(Music music) {
        mNotificationContentView = new RemoteViews(MyApplication.getBackgroundContext().getPackageName(), R.layout.notification_music_large);
        setupRemoteViews(music);
        Intent intent = new Intent(MyApplication.getBackgroundContext(), PlayMusicService.class);
        intent.putExtra(Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.PREVIOUS);
        PendingIntent previousMusicPendingIntent = createPendingIntentByExtra(intent, 0, Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.PREVIOUS);
        mNotificationContentView.setOnClickPendingIntent(R.id.notification_music_previous, previousMusicPendingIntent);

        PendingIntent playOrPauseMusicPendingIntent = createPendingIntentByExtra(intent, 1, Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.PLAY_OR_PAUSE);
        mNotificationContentView.setOnClickPendingIntent(R.id.notification_music_play_pause, playOrPauseMusicPendingIntent);

        PendingIntent nextMusicPendingIntent = createPendingIntentByExtra(intent, 2, Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.NEXT);
        mNotificationContentView.setOnClickPendingIntent(R.id.notification_music_next, nextMusicPendingIntent);

        PendingIntent singleClickPendingIntent = createPendingIntentByExtra(intent, 4, Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.SINGLE_CLICK);
        mNotificationContentView.setOnClickPendingIntent(R.id.notification, singleClickPendingIntent);
    }

    private void setupRemoteViews(Music music) {
        Bitmap cover = MusicCoverFileCache.getInstance().getCover(music.getMusicThumbAlbumCoverPath());
        if (cover != null) {
            mNotificationContentView.setImageViewBitmap(R.id.notification_music_cover, cover);
        } else {
            // no default cover in music file, set app default cover
            mNotificationContentView.setImageViewResource(R.id.notification_music_cover, R.mipmap.ic_launcher);
        }
        String musicName = music.getMusicName();
        if (musicName != null) {
            mNotificationContentView.setTextViewText(R.id.notification_music_name, musicName);
        }
        String musicArtist = music.getMusicArtist();
        if (musicArtist != null) {
            mNotificationContentView.setTextViewText(R.id.notification_music_artist, musicArtist);
        }
    }

    private PendingIntent createPendingIntentByExtra(Intent intent, int requestCode, String extra, String value) {
        intent.putExtra(extra, value);
        return PendingIntent.getService(MyApplication.getBackgroundContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
