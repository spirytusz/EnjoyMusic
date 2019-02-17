package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IBackgroundEventProcessor;
import com.zspirytus.enjoymusic.IBinderPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.impl.binder.PlayListChangeObserver;
import com.zspirytus.enjoymusic.impl.binder.PlayMusicChangeObserver;
import com.zspirytus.enjoymusic.impl.binder.PlayProgressChangeObserver;
import com.zspirytus.enjoymusic.impl.binder.PlayStateChangeObserver;

public class ForegroundBinderManager {

    private static class SingletonHolder {
        static ForegroundBinderManager INSTANCE = new ForegroundBinderManager();
    }

    private IBinderPool mBinderPool;

    public static ForegroundBinderManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(IBinderPool binderPool) {
        mBinderPool = binderPool;
        try {
            IBinder iBinder = mBinderPool.queryBinder(Constant.BinderCode.BACKGROUND_EVENT_PROCESSOR);
            IBackgroundEventProcessor backgroundEventProcessor = IBackgroundEventProcessor.Stub.asInterface(iBinder);
            backgroundEventProcessor.registerObserver(PlayStateChangeObserver.getInstance(), Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER);
            backgroundEventProcessor.registerObserver(PlayProgressChangeObserver.getInstance(), Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER);
            backgroundEventProcessor.registerObserver(PlayMusicChangeObserver.getInstance(), Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER);
            backgroundEventProcessor.registerObserver(PlayListChangeObserver.getInstance(), Constant.BinderCode.PLAY_LIST_OBSERVER);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public IBinder getBinderByBinderCode(int binderCode) {
        try {
            return mBinderPool.queryBinder(binderCode);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void release() {
        try {
            IBinder iBinder = mBinderPool.queryBinder(Constant.BinderCode.BACKGROUND_EVENT_PROCESSOR);
            IBackgroundEventProcessor backgroundEventProcessor = IBackgroundEventProcessor.Stub.asInterface(iBinder);
            backgroundEventProcessor.unregisterObserver(PlayStateChangeObserver.getInstance(), Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER);
            backgroundEventProcessor.unregisterObserver(PlayProgressChangeObserver.getInstance(), Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER);
            backgroundEventProcessor.unregisterObserver(PlayMusicChangeObserver.getInstance(), Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER);
            backgroundEventProcessor.unregisterObserver(PlayListChangeObserver.getInstance(), Constant.BinderCode.PLAY_LIST_OBSERVER);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBinderPool = null;
        SingletonHolder.INSTANCE = null;
    }

}
