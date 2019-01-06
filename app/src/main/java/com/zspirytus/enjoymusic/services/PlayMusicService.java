package com.zspirytus.enjoymusic.services;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;

import com.zspirytus.enjoymusic.base.BaseService;
import com.zspirytus.enjoymusic.cache.CurrentPlayingMusicCache;
import com.zspirytus.enjoymusic.cache.MusicSharedPreferences;
import com.zspirytus.enjoymusic.cache.PlayHistoryCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.BackgroundMusicController;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.impl.binder.IBinderPoolImpl;
import com.zspirytus.enjoymusic.receivers.MyHeadSetButtonClickBelowLReceiver;
import com.zspirytus.enjoymusic.receivers.MyHeadSetPlugOutReceiver;
import com.zspirytus.enjoymusic.services.media.MyMediaSession;
import com.zspirytus.enjoymusic.utils.LogUtil;
import com.zspirytus.enjoymusic.utils.StatusBarUtil;
import com.zspirytus.enjoymusic.view.activity.MainActivity;

/**
 * Service: 负责播放、暂停音乐、发送Notification相关事件
 * Created by ZSpirytus on 2018/8/2.
 */

public class PlayMusicService extends BaseService {

    private static final String TAG = "PlayMusicService";

    private IBinderPoolImpl mBinderPool;

    private MyHeadSetPlugOutReceiver myHeadSetPlugOutReceiver;
    private MyHeadSetButtonClickBelowLReceiver myHeadSetButtonClickBelowLReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e(this.getClass().getSimpleName(), "onCreate");
        MyMediaSession.getInstance().initMediaSession(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleStatusBarEvent(intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinderPool == null)
            mBinderPool = new IBinderPoolImpl();
        return mBinderPool;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicSharedPreferences.saveMusic(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
    }

    @Override
    protected void registerEvent() {
        myHeadSetPlugOutReceiver = new MyHeadSetPlugOutReceiver();
        IntentFilter headsetPlugOutFilter = new IntentFilter();
        headsetPlugOutFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(myHeadSetPlugOutReceiver, headsetPlugOutFilter);

        myHeadSetButtonClickBelowLReceiver = new MyHeadSetButtonClickBelowLReceiver();
        IntentFilter headsetButtonClickFilter = new IntentFilter();
        headsetButtonClickFilter.addAction(Intent.ACTION_MEDIA_BUTTON);
        registerReceiver(myHeadSetButtonClickBelowLReceiver, headsetButtonClickFilter);
    }

    @Override
    protected void unregisterEvent() {
        unregisterReceiver(myHeadSetPlugOutReceiver);

        unregisterReceiver(myHeadSetButtonClickBelowLReceiver);
    }

    private void handleStatusBarEvent(Intent intent) {
        if (intent != null) {
            String event = intent.getStringExtra(Constant.NotificationEvent.EXTRA);
            switch (event) {
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
                    BackgroundMusicController.getInstance().play(MusicPlayOrderManager.getInstance().getNextMusic(true));
                    break;
            }
        }
    }
}
