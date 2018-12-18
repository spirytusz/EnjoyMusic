package com.zspirytus.enjoymusic.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.graphics.Palette;
import android.widget.RemoteViews;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.cache.MyApplication;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.utils.BitmapUtil;
import com.zspirytus.enjoymusic.utils.DrawableUtil;
import com.zspirytus.enjoymusic.utils.LogUtil;
import com.zspirytus.enjoymusic.utils.OSUtils;

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
    private RemoteViews mBigView;
    private RemoteViews mView;

    private Bitmap previous;
    private Bitmap play;
    private Bitmap pause;
    private Bitmap next;

    private NotificationHelper() {
        Context backgroundContext = MyApplication.getBackgroundContext();
        mNotificationManager = (NotificationManager) backgroundContext.getSystemService(NOTIFICATION_SERVICE);
    }

    public static NotificationHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void showNotification(Music music) {
        if (mChannelId == null) {
            createNotificationChannel();
        }
        if (mBigView == null) {
            createNotificationView(music);
            createNotification();
        } else {
            setupRemoteViewsAndBackground(music);
        }
    }

    public void setPlayOrPauseBtnRes(boolean isPlaying) {
        if (mCurrentNotification != null) {
            Bitmap bitmap = isPlaying ? pause : play;
            if (mBigView != null) {
                mBigView.setImageViewBitmap(R.id.notification_music_play_pause, bitmap);
            }
            if (mView != null) {
                mView.setImageViewBitmap(R.id.notification_normal_play_or_pause, bitmap);
            }
            mNotificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, mCurrentNotification);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannelId = "music_notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(mChannelId, "音乐通知栏", importance);
            mNotificationManager.createNotificationChannel(channel);
        } else {
            mChannelId = "default";
        }
    }

    private void createNotification() {
        mCurrentNotification = new NotificationCompat.Builder(MyApplication.getBackgroundContext(), mChannelId)
                .setSmallIcon(R.drawable.ic_music_note_white_24dp)
                .setCustomContentView(mView)
                .setCustomBigContentView(mBigView)
                .setOngoing(true)
                .setTicker("Music Playing")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();
        String osName = OSUtils.getRomType().name();
        if (osName != null && osName.equals("MIUI")) {
            prepareNotificationForMIUI(mCurrentNotification);
        }
    }

    private void createNotificationView(Music music) {
        mBigView = new RemoteViews(MyApplication.getBackgroundContext().getPackageName(), R.layout.notification_music_large);
        mView = new RemoteViews(MyApplication.getBackgroundContext().getPackageName(), R.layout.notification_music_normal);
        setupRemoteViewsAndBackground(music);
        Intent intent = new Intent(MyApplication.getBackgroundContext(), PlayMusicService.class);
        intent.putExtra(Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.PREVIOUS);
        PendingIntent previousMusicPendingIntent = createPendingIntentByExtra(intent, 0, Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.PREVIOUS);
        mBigView.setOnClickPendingIntent(R.id.notification_music_previous, previousMusicPendingIntent);

        PendingIntent playOrPauseMusicPendingIntent = createPendingIntentByExtra(intent, 1, Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.PLAY_OR_PAUSE);
        mBigView.setOnClickPendingIntent(R.id.notification_music_play_pause, playOrPauseMusicPendingIntent);
        mView.setOnClickPendingIntent(R.id.notification_normal_play_or_pause, playOrPauseMusicPendingIntent);

        PendingIntent nextMusicPendingIntent = createPendingIntentByExtra(intent, 2, Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.NEXT);
        mBigView.setOnClickPendingIntent(R.id.notification_music_next, nextMusicPendingIntent);
        mView.setOnClickPendingIntent(R.id.notification_normal_next, nextMusicPendingIntent);

        PendingIntent singleClickPendingIntent = createPendingIntentByExtra(intent, 4, Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.SINGLE_CLICK);
        mBigView.setOnClickPendingIntent(R.id.notification, singleClickPendingIntent);
        mView.setOnClickPendingIntent(R.id.notification_normal, singleClickPendingIntent);
    }

    private void setupRemoteViews(Music music, int backgroundColor, int panelColor) {
        Bitmap cover = MusicCoverFileCache.getInstance().getCover(music.getMusicThumbAlbumCoverPath());
        if (cover != null) {
            mView.setImageViewBitmap(R.id.notification_normal_music_cover, cover);
            if (backgroundColor != 0) {
                mView.setInt(R.id.notification_normal, "setBackgroundColor", backgroundColor);
            } else {
                mView.setInt(R.id.notification_normal, "setBackgroundColor", R.color.white);
            }
            if (panelColor != 0) {
                mView.setInt(R.id.notification_normal_music_name, "setTextColor", panelColor);
                mView.setInt(R.id.notification_normal_music_artist, "setTextColor", panelColor);
                mView.setImageViewBitmap(R.id.notification_normal_play_or_pause, pause);
                mView.setImageViewBitmap(R.id.notification_normal_next, next);
            } else {
                play = BitmapUtil.createBitmapByResId(R.drawable.ic_play_arrow_black_48dp);
                pause = BitmapUtil.createBitmapByResId(R.drawable.ic_pause_black_48dp);
            }
        } else {
            mView.setImageViewResource(R.id.notification_music_cover, R.mipmap.ic_launcher);
            play = BitmapUtil.createBitmapByResId(R.drawable.ic_play_arrow_black_48dp);
            pause = BitmapUtil.createBitmapByResId(R.drawable.ic_pause_black_48dp);
        }
        String musicName = music.getMusicName();
        if (musicName != null) {
            mView.setTextViewText(R.id.notification_music_name, musicName);
        }
        String musicArtist = music.getMusicArtist();
        if (musicArtist != null) {
            mView.setTextViewText(R.id.notification_music_artist, musicArtist);
        }
        mNotificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, mCurrentNotification);
    }

    private void setupBigRemoteViews(Music music, int backgroundColor, int panelColor) {
        Bitmap cover = MusicCoverFileCache.getInstance().getCover(music.getMusicThumbAlbumCoverPath());
        if (cover != null) {
            mBigView.setImageViewBitmap(R.id.notification_music_cover, cover);
            if (backgroundColor != 0) {
                mBigView.setInt(R.id.notification, "setBackgroundColor", backgroundColor);
            } else {
                mBigView.setInt(R.id.notification, "setBackgroundColor", R.color.white);
            }
            if (panelColor != 0) {
                mBigView.setInt(R.id.notification_music_name, "setTextColor", panelColor);
                mBigView.setInt(R.id.notification_music_artist, "setTextColor", panelColor);
                previous = BitmapUtil.drawableToBitmap(DrawableUtil.setColor(MyApplication.getBackgroundContext(), R.drawable.ic_skip_previous_black_48dp, panelColor));
                play = BitmapUtil.drawableToBitmap(DrawableUtil.setColor(MyApplication.getBackgroundContext(), R.drawable.ic_play_arrow_black_48dp, panelColor));
                pause = BitmapUtil.drawableToBitmap(DrawableUtil.setColor(MyApplication.getBackgroundContext(), R.drawable.ic_pause_black_48dp, panelColor));
                next = BitmapUtil.drawableToBitmap(DrawableUtil.setColor(MyApplication.getBackgroundContext(), R.drawable.ic_skip_next_black_48dp, panelColor));
                mBigView.setImageViewBitmap(R.id.notification_music_previous, previous);
                mBigView.setImageViewBitmap(R.id.notification_music_play_pause, pause);
                mBigView.setImageViewBitmap(R.id.notification_music_next, next);
            } else {
                play = BitmapUtil.createBitmapByResId(R.drawable.ic_play_arrow_black_48dp);
                pause = BitmapUtil.createBitmapByResId(R.drawable.ic_pause_black_48dp);
            }
        } else {
            mBigView.setImageViewResource(R.id.notification_music_cover, R.mipmap.ic_launcher);
            play = BitmapUtil.createBitmapByResId(R.drawable.ic_play_arrow_black_48dp);
            pause = BitmapUtil.createBitmapByResId(R.drawable.ic_pause_black_48dp);
        }
        String musicName = music.getMusicName();
        if (musicName != null) {
            mBigView.setTextViewText(R.id.notification_music_name, musicName);
        }
        String musicArtist = music.getMusicArtist();
        if (musicArtist != null) {
            mBigView.setTextViewText(R.id.notification_music_artist, musicArtist);
        }
        mNotificationManager.notify(NOTIFICATION_MANAGER_NOTIFY_ID, mCurrentNotification);
    }

    private void setupRemoteViewsAndBackground(final Music music) {
        Bitmap cover = MusicCoverFileCache.getInstance().getCover(music.getMusicThumbAlbumCoverPath());
        if (cover != null) {
            Palette.from(cover).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch color = palette.getDarkVibrantSwatch();
                    if (color != null) {
                        setupBigRemoteViews(music, color.getRgb(), color.getTitleTextColor());
                        setupRemoteViews(music, color.getRgb(), color.getTitleTextColor());
                    } else {
                        setupBigRemoteViews(music, 0, 0);
                        setupRemoteViews(music, 0, 0);
                    }
                }
            });
        } else {
            setupBigRemoteViews(music, 0, 0);
            setupRemoteViews(music, 0, 0);
        }
    }

    private PendingIntent createPendingIntentByExtra(Intent intent, int requestCode, String extra, String value) {
        intent.putExtra(extra, value);
        return PendingIntent.getService(MyApplication.getBackgroundContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
