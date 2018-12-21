package com.zspirytus.enjoymusic.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.services.media.MyMediaSession;
import com.zspirytus.enjoymusic.utils.DeviceUtils;
import com.zspirytus.enjoymusic.utils.LogUtil;

import java.lang.reflect.Field;

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

    private final NotificationManager mNotificationManager;

    private Notification mCurrentNotification;
    private String mChannelId;

    private Notification.Action playAction;
    private Notification.Action pauseAction;

    private NotificationHelper() {
        Context backgroundContext = MainApplication.getBackgroundContext();
        mNotificationManager = (NotificationManager) backgroundContext.getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(MainApplication.getBackgroundContext(), PlayMusicService.class);
        intent.putExtra(Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.PREVIOUS);
        PendingIntent play = createPendingIntentByExtra(intent, 4, Constant.NotificationEvent.PLAY);
        playAction = new Notification.Action(R.drawable.ic_play_arrow_black_48dp, "play", play);
        PendingIntent pause = createPendingIntentByExtra(intent, 1, Constant.NotificationEvent.PAUSE);
        pauseAction = new Notification.Action(R.drawable.ic_pause_black_48dp, "pause", pause);
    }

    public static NotificationHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void showNotification(Music music) {
        if (mChannelId == null) {
            createNotificationChannel();
        }
        createNotification(music);
        mNotificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, mCurrentNotification);
    }

    public void updateNotification(boolean isPlaying) {
        if (mCurrentNotification != null) {
            if (isPlaying) {
                mCurrentNotification.actions[1] = pauseAction;
            } else {
                mCurrentNotification.actions[1] = playAction;
            }
            mNotificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, mCurrentNotification);
        }
    }

    private void createNotificationChannel() {
        if (DeviceUtils.isOSVersionHigherThan(Build.VERSION_CODES.O)) {
            mChannelId = "music_notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(mChannelId, "音乐通知栏", importance);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setSound(null, null);
            channel.enableLights(false);
            channel.enableVibration(false);
            mNotificationManager.createNotificationChannel(channel);
        } else {
            mChannelId = "default";
        }
    }

    private void createNotification(Music music) {
        MediaStyle mediaStyle = new MediaStyle();
        mediaStyle.setMediaSession(MyMediaSession.getInstance().getSessionToken());
        mediaStyle.setShowCancelButton(true);
        NotificationCompat.Builder builder = getDefaultBuilder(music);
        builder.setStyle(mediaStyle);
        builder = createNotificationAction(builder);
        mCurrentNotification = builder.build();
        String osName = DeviceUtils.getOSName();
        if (osName != null && osName.equals("MIUI")) {
            prepareNotificationForMIUI(mCurrentNotification);
        }
    }

    private NotificationCompat.Builder getDefaultBuilder(Music music) {
        Intent intent = new Intent(MainApplication.getBackgroundContext(), PlayMusicService.class);
        PendingIntent startActivity = createPendingIntentByExtra(intent, 5, Constant.NotificationEvent.SINGLE_CLICK);
        return new NotificationCompat.Builder(MainApplication.getBackgroundContext(), mChannelId)
                .setSmallIcon(R.drawable.ic_music_note_white_24dp)
                .setLargeIcon(MusicCoverFileCache.getInstance().getCover(music.getMusicThumbAlbumCoverPath()))
                .setContentTitle(music.getMusicName())
                .setContentText(music.getMusicArtist())
                .setChannelId(mChannelId)
                .setAutoCancel(false)
                .setOngoing(true)
                .setColorized(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setContentIntent(startActivity);
    }

    private NotificationCompat.Builder createNotificationAction(NotificationCompat.Builder builder) {
        Intent intent = new Intent(MainApplication.getBackgroundContext(), PlayMusicService.class);
        PendingIntent pause = createPendingIntentByExtra(intent, 0, Constant.NotificationEvent.PAUSE);
        PendingIntent next = createPendingIntentByExtra(intent, 1, Constant.NotificationEvent.NEXT);
        PendingIntent previous = createPendingIntentByExtra(intent, 2, Constant.NotificationEvent.PREVIOUS);
        builder.addAction(new NotificationCompat.Action(R.drawable.ic_skip_previous_black_48dp, "previous", previous))
                .addAction(new NotificationCompat.Action(R.drawable.ic_pause_black_48dp, "pause", pause))
                .addAction(new NotificationCompat.Action(R.drawable.ic_skip_next_black_48dp, "next", next));
        return builder;
    }

    private PendingIntent createPendingIntentByExtra(Intent intent, int requestCode, String value) {
        intent.putExtra(Constant.NotificationEvent.EXTRA, value);
        return PendingIntent.getService(MainApplication.getBackgroundContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void prepareNotificationForMIUI(Notification notification) {
        try {
            @SuppressLint("PrivateApi")
            Object miuiNotification = Class.forName("android.app.MiuiNotification").newInstance();
            Field customizedIconField = miuiNotification.getClass().getDeclaredField("customizedIcon");
            customizedIconField.setAccessible(true);
            customizedIconField.set(miuiNotification, true);

            Field extraNotificationField = notification.getClass().getField("extraNotification");
            extraNotificationField.setAccessible(true);
            extraNotificationField.set(notification, miuiNotification);
        } catch (Exception e) {
            LogUtil.e(this.getClass().getSimpleName(), "OS is not MIUI");
        }
    }

}
