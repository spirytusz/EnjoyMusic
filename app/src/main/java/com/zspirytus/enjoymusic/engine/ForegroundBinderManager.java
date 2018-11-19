package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.BinderPool;
import com.zspirytus.enjoymusic.adapter.binder.IMusicPlayCompleteObserverImpl;
import com.zspirytus.enjoymusic.adapter.binder.IPlayProgressChangeObserverImpl;
import com.zspirytus.enjoymusic.adapter.binder.IPlayStateChangeObserverImpl;
import com.zspirytus.enjoymusic.cache.constant.Constant;

public class ForegroundBinderManager {

    private static class SingletonHolder {
        static ForegroundBinderManager INSTANCE = new ForegroundBinderManager();
    }

    private BinderPool mBinderPool;

    public static ForegroundBinderManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(BinderPool binderPool) {
        mBinderPool = binderPool;
        try {
            mBinderPool.registerObserver(IPlayStateChangeObserverImpl.getInstance(), Constant.BinderPoolCode.BINDER_POOL_MUSIC_PLAY_STATE_CHANGE_OBSERVER);
            mBinderPool.registerObserver(IPlayProgressChangeObserverImpl.getInstance(), Constant.BinderPoolCode.BINDER_POOL_MUSIC_PROGRESS_OBSERVER);
            mBinderPool.registerObserver(IMusicPlayCompleteObserverImpl.getInstance(), Constant.BinderPoolCode.BINDER_POOL_MUSIC_PLAY_COMPLETE_OBSERVER);
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
            mBinderPool.unregisterObserver(IPlayStateChangeObserverImpl.getInstance(), Constant.BinderPoolCode.BINDER_POOL_MUSIC_PLAY_STATE_CHANGE_OBSERVER);
            mBinderPool.unregisterObserver(IPlayProgressChangeObserverImpl.getInstance(), Constant.BinderPoolCode.BINDER_POOL_MUSIC_PROGRESS_OBSERVER);
            mBinderPool.unregisterObserver(IMusicPlayCompleteObserverImpl.getInstance(), Constant.BinderPoolCode.BINDER_POOL_MUSIC_PLAY_COMPLETE_OBSERVER);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBinderPool = null;
        SingletonHolder.INSTANCE = null;
    }

}
