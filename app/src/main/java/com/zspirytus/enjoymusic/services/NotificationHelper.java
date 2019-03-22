package com.zspirytus.enjoymusic.services;

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
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.services.media.MyMediaSession;
import com.zspirytus.enjoymusic.utils.DeviceUtils;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Notification帮助类，负责显示、更新Notification
 * Created by ZSpirytus on 2018/8/11.
 */

public class NotificationHelper {

    private static final String TAG = "NotificationHelper";

    private static final int PREVIOUS = 0;
    private static final int PLAY = 1;
    private static final int PAUSE = 2;
    private static final int NEXT = 3;
    private static final int SINGLE_CLICK = 4;
    private static final int DELETE = 5;

    private static final long AUTO_CANCEL_DELAY = 10 * 60 * 1000;

    private static class SingletonHolder {
        static NotificationHelper INSTANCE = new NotificationHelper();
    }

    private static final int NOTIFICATION_MANAGER_NOTIFY_ID = 233;

    private NotificationCompat.Builder mBuilder;
    private final NotificationManager mNotificationManager;

    private Notification mCurrentNotification;
    private String mChannelId;

    private Notification.Action playAction;
    private Notification.Action pauseAction;

    Notification getCurrentNotification() {
        return mCurrentNotification;
    }

    private NotificationHelper() {
        Context backgroundContext = MainApplication.getAppContext();
        mNotificationManager = (NotificationManager) backgroundContext.getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(MainApplication.getAppContext(), PlayMusicService.class);
        intent.putExtra(Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.PREVIOUS);
        PendingIntent play = createPendingIntentByExtra(intent, PLAY, Constant.NotificationEvent.PLAY);
        playAction = new Notification.Action(R.drawable.ic_play_arrow_black_48dp, "play", play);
        PendingIntent pause = createPendingIntentByExtra(intent, PAUSE, Constant.NotificationEvent.PAUSE);
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
            /*
             * 这里直接替换第二个action即可，
             * 因为只有中间的按钮(播放、暂停)的响应Intent和ResId有变化
             */
            if (isPlaying) {
                mCurrentNotification.actions[1] = pauseAction;
            } else {
                mCurrentNotification.actions[1] = playAction;
            }
            mNotificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, mCurrentNotification);
        }
    }

    int getNotificationNotifyId() {
        return NOTIFICATION_MANAGER_NOTIFY_ID;
    }

    void setCancelable(boolean canClear) {
        if (mCurrentNotification != null) {
            mCurrentNotification = mBuilder.setAutoCancel(canClear).build();
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
        NotificationCompat.Builder builder = createNotificationAction(getDefaultBuilder(music));
        mCurrentNotification = builder.build();
    }

    private NotificationCompat.Builder getDefaultBuilder(Music music) {
        MediaStyle mediaStyle = new MediaStyle();
        mediaStyle.setMediaSession(MyMediaSession.getInstance().getSessionToken())
                /*
                 * 当折叠的时候，显示第1, 2, 3个按钮
                 */
                .setShowActionsInCompactView(0, 1, 2);
        mediaStyle.setShowCancelButton(true);

        Album album = QueryExecutor.findAlbum(music);
        Artist artist = QueryExecutor.findArtist(music);

        Intent intent = new Intent(MainApplication.getAppContext(), PlayMusicService.class);
        PendingIntent startActivity = createPendingIntentByExtra(intent, SINGLE_CLICK, Constant.NotificationEvent.SINGLE_CLICK);
        Intent deleteIntent = new Intent(MainApplication.getAppContext(), PlayMusicService.class);
        PendingIntent deletePendingIntent = createPendingIntentByExtra(deleteIntent, DELETE, Constant.NotificationEvent.DELETE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainApplication.getAppContext(), mChannelId)
                .setSmallIcon(R.drawable.ic_music_note_white_24dp)
                .setLargeIcon(MusicCoverFileCache.getInstance().getCoverBitmap(album))
                .setContentTitle(music.getMusicName())
                .setContentText(artist.getArtistName())
                .setStyle(mediaStyle)
                .setChannelId(mChannelId)
                .setAutoCancel(false)
                .setDeleteIntent(deletePendingIntent)
                .setColorized(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setContentIntent(startActivity);

        mBuilder = createNotificationAction(builder);

        return mBuilder;
    }

    private NotificationCompat.Builder createNotificationAction(NotificationCompat.Builder builder) {
        Intent intent = new Intent(MainApplication.getAppContext(), PlayMusicService.class);
        PendingIntent previous = createPendingIntentByExtra(intent, PREVIOUS, Constant.NotificationEvent.PREVIOUS);
        PendingIntent pause = createPendingIntentByExtra(intent, PAUSE, Constant.NotificationEvent.PAUSE);
        PendingIntent next = createPendingIntentByExtra(intent, NEXT, Constant.NotificationEvent.NEXT);
        builder.mActions.clear();
        builder.addAction(new NotificationCompat.Action(R.drawable.ic_skip_previous_black_48dp, "previous", previous))
                .addAction(new NotificationCompat.Action(R.drawable.ic_pause_black_48dp, "pause", pause))
                .addAction(new NotificationCompat.Action(R.drawable.ic_skip_next_black_48dp, "next", next));
        return builder;
    }

    private PendingIntent createPendingIntentByExtra(Intent intent, int requestCode, String value) {
        intent.putExtra(Constant.NotificationEvent.EXTRA, value);
        return PendingIntent.getService(MainApplication.getAppContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
