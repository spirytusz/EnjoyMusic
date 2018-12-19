package com.zspirytus.enjoymusic.services;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;

import com.zspirytus.enjoymusic.BinderPool;
import com.zspirytus.enjoymusic.adapter.binder.IGetMusicListImpl;
import com.zspirytus.enjoymusic.adapter.binder.IMusicControlImpl;
import com.zspirytus.enjoymusic.adapter.binder.IMusicProgressControlImpl;
import com.zspirytus.enjoymusic.adapter.binder.ISetPlayListImpl;
import com.zspirytus.enjoymusic.cache.CurrentPlayingMusicCache;
import com.zspirytus.enjoymusic.cache.MusicSharedPreferences;
import com.zspirytus.enjoymusic.cache.PlayHistoryCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.BackgroundMusicController;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayProgressChangeObserver;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayStateChangeObserver;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.interfaces.RemotePlayMusicChangeCallback;
import com.zspirytus.enjoymusic.interfaces.RemotePlayProgressCallback;
import com.zspirytus.enjoymusic.interfaces.RemotePlayStateChangeCallback;
import com.zspirytus.enjoymusic.receivers.MyHeadSetButtonClickBelowLReceiver;
import com.zspirytus.enjoymusic.receivers.MyHeadSetPlugOutReceiver;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.enjoymusic.services.media.MyMediaSession;
import com.zspirytus.enjoymusic.utils.StatusBarUtil;
import com.zspirytus.enjoymusic.view.activity.MainActivity;

/**
 * Service: 负责播放、暂停音乐、发送Notification相关事件
 * Created by ZSpirytus on 2018/8/2.
 */

public class PlayMusicService extends BaseService implements RemotePlayProgressCallback,
        RemotePlayStateChangeCallback, RemotePlayMusicChangeCallback {

    private BinderPoolImpl mBinderPool;

    private MyHeadSetPlugOutReceiver myHeadSetPlugOutReceiver;
    private MyHeadSetButtonClickBelowLReceiver myHeadSetButtonClickBelowLReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        MyMediaSession.getInstance().initMediaSession(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleStatusBarEvent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinderPool == null)
            mBinderPool = new BinderPoolImpl();
        return mBinderPool;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicSharedPreferences.saveMusic(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
    }

    @Override
    public void onPlayStateChange(boolean isPlaying) {
        NotificationHelper.getInstance().updateNotification(isPlaying);
        notifyAllObserverPlayStateChange(isPlaying);
    }

    @Override
    public void onProgressChange(int progress) {
        notifyAllObserverMusicPlayProgressChange(progress);
    }

    @Override
    public void onPlayMusicChange(Music currentPlayingMusic) {
        NotificationHelper.getInstance().showNotification(currentPlayingMusic);
        if (notifyAllObserverPlayMusicChange(currentPlayingMusic) == 0) {
            // handle play music logic by service
        }
    }

    @Override
    protected void registerEvent() {
        MediaPlayController.getInstance().setPlayProgressCallback(this);
        MediaPlayController.getInstance().setPlayStateChangeCallback(this);
        MediaPlayController.getInstance().setMusicPlayCompleteCallback(this);

        myHeadSetPlugOutReceiver = new MyHeadSetPlugOutReceiver();
        IntentFilter headsetPlugOutFilter = new IntentFilter();
        headsetPlugOutFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(myHeadSetPlugOutReceiver, headsetPlugOutFilter);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            myHeadSetButtonClickBelowLReceiver = new MyHeadSetButtonClickBelowLReceiver();
            IntentFilter headsetButtonClickFilter = new IntentFilter();
            headsetButtonClickFilter.addAction(Intent.ACTION_MEDIA_BUTTON);
            registerReceiver(myHeadSetButtonClickBelowLReceiver, headsetButtonClickFilter);
        }
    }

    @Override
    protected void unregisterEvent() {
        MediaPlayController.getInstance().setPlayProgressCallback(null);
        MediaPlayController.getInstance().setPlayStateChangeCallback(null);
        MediaPlayController.getInstance().setMusicPlayCompleteCallback(null);

        unregisterReceiver(myHeadSetPlugOutReceiver);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            unregisterReceiver(myHeadSetButtonClickBelowLReceiver);
        }
    }

    private void handleStatusBarEvent(Intent intent) {
        String value = intent.getStringExtra(Constant.NotificationEvent.EXTRA);
        switch (value) {
            case Constant.NotificationEvent.SINGLE_CLICK:
                MainActivity.startActivity(this, Constant.NotificationEvent.EXTRA, Constant.NotificationEvent.ACTION_NAME);
                StatusBarUtil.collapseStatusBar(this);
                break;
            case Constant.NotificationEvent.PREVIOUS:
                BackgroundMusicController.getInstance().play(PlayHistoryCache.getInstance().getPreviousPlayedMusic());
                break;
            case Constant.NotificationEvent.PLAY:
                BackgroundMusicController.getInstance().play(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
                break;
            case Constant.NotificationEvent.PAUSE:
                BackgroundMusicController.getInstance().pause();
                break;
            case Constant.NotificationEvent.NEXT:
                BackgroundMusicController.getInstance().play(MusicPlayOrderManager.getInstance().getNextMusic());
                break;
        }
    }

    private class BinderPoolImpl extends BinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) {
            switch (binderCode) {
                case Constant.BinderCode.MUSIC_CONTROL:
                    return IMusicControlImpl.getInstance();
                case Constant.BinderCode.MUSIC_PROGRESS_CONTROL:
                    return IMusicProgressControlImpl.getInstance();
                case Constant.BinderCode.GET_MUSIC_LIST:
                    return IGetMusicListImpl.getInstance();
                case Constant.BinderCode.SET_PLAY_LIST:
                    return ISetPlayListImpl.getInstance();
                default:
                    return null;
            }
        }

        @Override
        public void registerObserver(IBinder observer, int binderCode) {
            switch (binderCode) {
                case Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER:
                    registerPlayStateObserver(IPlayStateChangeObserver.Stub.asInterface(observer));
                    break;
                case Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER:
                    registerProgressChangeObserver(IPlayProgressChangeObserver.Stub.asInterface(observer));
                    break;
                case Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER:
                    registerMusicPlayCompleteObserver(IPlayedMusicChangeObserver.Stub.asInterface(observer));
                    break;
            }
        }

        @Override
        public void unregisterObserver(IBinder observer, int binderCode) {
            switch (binderCode) {
                case Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER:
                    unregisterPlayStateObserver(IPlayStateChangeObserver.Stub.asInterface(observer));
                    break;
                case Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER:
                    unregisterProgressChangeObserver(IPlayProgressChangeObserver.Stub.asInterface(observer));
                    break;
                case Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER:
                    unregisterMusicPlayCompleteObserver(IPlayedMusicChangeObserver.Stub.asInterface(observer));
                    break;
            }
        }
    }
}
