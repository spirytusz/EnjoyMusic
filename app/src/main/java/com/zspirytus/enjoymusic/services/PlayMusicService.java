package com.zspirytus.enjoymusic.services;

import android.app.Notification;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;

import com.zspirytus.enjoymusic.base.BaseService;
import com.zspirytus.enjoymusic.cache.BackgroundMusicStateCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.BackgroundMusicController;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.impl.binder.BinderPool;
import com.zspirytus.enjoymusic.listeners.IOnRemotePauseListener;
import com.zspirytus.enjoymusic.listeners.IOnRemotePlayListener;
import com.zspirytus.enjoymusic.listeners.IOnRemoteProgressListener;
import com.zspirytus.enjoymusic.receivers.MyAlarm;
import com.zspirytus.enjoymusic.receivers.MyHeadSetPlugOutReceiver;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.enjoymusic.services.media.MyMediaSession;
import com.zspirytus.enjoymusic.utils.StatusBarUtil;
import com.zspirytus.enjoymusic.view.activity.MainActivity;

import java.util.Calendar;

/**
 * Service: 负责播放、暂停音乐、发送Notification相关事件
 * Created by ZSpirytus on 2018/8/2.
 */

public class PlayMusicService extends BaseService implements IOnRemotePlayListener, IOnRemotePauseListener, IOnRemoteProgressListener {

    private static final String TAG = "PlayMusicService";

    private BinderPool mBinderPool;

    private MyHeadSetPlugOutReceiver myHeadSetPlugOutReceiver;
    private MyAlarm myAlarm;

    @Override
    public void onCreate() {
        super.onCreate();
        MyMediaSession.getInstance().initMediaSession(this);
        MediaPlayController.getInstance().setOnPlayListener(this);
        MediaPlayController.getInstance().setOnPauseListener(this);
        MediaPlayController.getInstance().registerRemotePlayProgressObserver(this);
        BackgroundMusicStateCache.getInstance().init();

        myAlarm = new MyAlarm();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleStatusBarEvent(intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinderPool == null) {
            mBinderPool = new BinderPool();
        }
        return mBinderPool;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mBinderPool = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void registerEvent() {
        myHeadSetPlugOutReceiver = new MyHeadSetPlugOutReceiver();
        IntentFilter headsetPlugOutFilter = new IntentFilter();
        headsetPlugOutFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(myHeadSetPlugOutReceiver, headsetPlugOutFilter);
    }

    @Override
    protected void unregisterEvent() {
        unregisterReceiver(myHeadSetPlugOutReceiver);
    }

    @Override
    public void onPlay(Music music) {
        Notification currentNotification = NotificationHelper.getInstance().getCurrentNotification();
        int notificationNotifyId = NotificationHelper.getInstance().getNotificationNotifyId();
        /*
         * notificationNotifyId 不能为0
         * @see #startForeground(int, Notification)
         */
        startForeground(notificationNotifyId, currentNotification);
        NotificationHelper.getInstance().setCancelable(false);
        /*UIThreadSwitcher.runOnMainThreadSync(() -> {
            Lyric lyric = DBManager.getInstance().getDaoSession().getLyricDao().load(music.getMusicId());
            String lyricFilePath = lyric != null ? lyric.getLyricFilePath() : null;
            LyricWidgetManager.getInstance().loadLyric(lyricFilePath);
        });*/

        if (!isActivityInForeground()) {
            myAlarm.cancelAlarm(this);
        }
    }

    @Override
    public void onPause() {
        stopForeground(false);
        NotificationHelper.getInstance().setCancelable(true);

        if (!isActivityInForeground()) {
            final int field = Calendar.MINUTE;
            final int amount = 5;
            myAlarm.setAlarm(this, field, amount);
        }
    }

    @Override
    public void onProgressChange(long milliseconds) {
        //UIThreadSwitcher.runOnMainThreadSync(() -> LyricWidgetManager.getInstance().onProgressChange(milliseconds));
    }

    private void handleStatusBarEvent(Intent intent) {
        if (intent != null) {
            String event = intent.getStringExtra(Constant.NotificationEvent.EXTRA);
            if (event != null) {
                switch (event) {
                    case Constant.NotificationEvent.SINGLE_CLICK:
                        startActivity();
                        StatusBarUtil.collapseStatusBar(this);
                        break;
                    case Constant.NotificationEvent.PREVIOUS:
                        BackgroundMusicController.getInstance().play(MusicPlayOrderManager.getInstance().getPreviousMusic());
                        break;
                    case Constant.NotificationEvent.PLAY:
                        BackgroundMusicController.getInstance().play(BackgroundMusicStateCache.getInstance().getCurrentPlayingMusic());
                        break;
                    case Constant.NotificationEvent.PAUSE:
                        BackgroundMusicController.getInstance().pause();
                        break;
                    case Constant.NotificationEvent.NEXT:
                        BackgroundMusicController.getInstance().play(MusicPlayOrderManager.getInstance().getNextMusic(true));
                        break;
                    case Constant.NotificationEvent.DELETE:
                        if (!isActivityInForeground()) {
                            BackgroundMusicController.getInstance().release();
                        }
                        break;
                }
            }
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isActivityInForeground() {
        return mBinderPool != null;
    }

    private void startActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        Music music = BackgroundMusicStateCache.getInstance().getCurrentPlayingMusic();
        intent.putExtra(Constant.NotificationEvent.EXTRA, music);
        this.startActivity(intent);
    }
}
