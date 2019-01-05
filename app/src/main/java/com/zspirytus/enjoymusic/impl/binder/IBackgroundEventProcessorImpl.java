package com.zspirytus.enjoymusic.impl.binder;

import android.os.IBinder;

import com.zspirytus.enjoymusic.IBackgroundEventProcessor;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayProgressChangeObserver;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayStateChangeObserver;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

public class IBackgroundEventProcessorImpl extends IBackgroundEventProcessor.Stub {

    private static IBackgroundEventProcessorImpl INSTANCE
            = new IBackgroundEventProcessorImpl();

    public static IBackgroundEventProcessorImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void registerObserver(IBinder observer, int binderCode) {
        MediaPlayController mediaPlayController = MediaPlayController.getInstance();
        switch (binderCode) {
            case Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER:
                mediaPlayController.registerPlayStateObserver(IPlayStateChangeObserver.Stub.asInterface(observer));
                break;
            case Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER:
                mediaPlayController.registerProgressChangeObserver(IPlayProgressChangeObserver.Stub.asInterface(observer));
                break;
            case Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER:
                mediaPlayController.registerMusicPlayCompleteObserver(IPlayedMusicChangeObserver.Stub.asInterface(observer));
                break;
        }
    }

    @Override
    public void unregisterObserver(IBinder observer, int binderCode) {
        MediaPlayController mediaPlayController = MediaPlayController.getInstance();
        switch (binderCode) {
            case Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER:
                mediaPlayController.unregisterPlayStateObserver(IPlayStateChangeObserver.Stub.asInterface(observer));
                break;
            case Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER:
                mediaPlayController.unregisterProgressChangeObserver(IPlayProgressChangeObserver.Stub.asInterface(observer));
                break;
            case Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER:
                mediaPlayController.unregisterMusicPlayCompleteObserver(IPlayedMusicChangeObserver.Stub.asInterface(observer));
                break;
        }
    }
}
