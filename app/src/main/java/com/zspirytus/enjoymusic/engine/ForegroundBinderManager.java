package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IBackgroundEventProcessor;
import com.zspirytus.enjoymusic.IBinderPool;
import com.zspirytus.enjoymusic.adapter.binder.IPlayMusicChangeObserverImpl;
import com.zspirytus.enjoymusic.adapter.binder.IPlayProgressChangeObserverImpl;
import com.zspirytus.enjoymusic.adapter.binder.IPlayStateChangeObserverImpl;
import com.zspirytus.enjoymusic.cache.constant.Constant;

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
            backgroundEventProcessor.registerObserver(IPlayStateChangeObserverImpl.getInstance(), Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER);
            backgroundEventProcessor.registerObserver(IPlayProgressChangeObserverImpl.getInstance(), Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER);
            backgroundEventProcessor.registerObserver(IPlayMusicChangeObserverImpl.getInstance(), Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER);
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
            backgroundEventProcessor.unregisterObserver(IPlayStateChangeObserverImpl.getInstance(), Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER);
            backgroundEventProcessor.unregisterObserver(IPlayProgressChangeObserverImpl.getInstance(), Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER);
            backgroundEventProcessor.unregisterObserver(IPlayMusicChangeObserverImpl.getInstance(), Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBinderPool = null;
        SingletonHolder.INSTANCE = null;
    }

}
