package com.zspirytus.enjoymusic.adapter.binder;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.BinderPool;
import com.zspirytus.enjoymusic.IMusicControl;
import com.zspirytus.enjoymusic.cache.constant.Constant;

public class BinderPoolImpl extends BinderPool.Stub {

    private IBinder mBinder;

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        switch (binderCode) {
            case Constant.BinderPoolCode.BINDER_POOL_MUSIC_CONTROL:
                if (mBinder != null && mBinder instanceof IMusicControl) {
                    return mBinder;
                } else {
                    mBinder = new IMusicControlImpl();
                    return mBinder;
                }
            case Constant.BinderPoolCode.BINDER_POOL_MUSIC_PROGRESS_CONTROL:
                if (mBinder != null && mBinder instanceof IMusicProgressControlImpl) {
                    return mBinder;
                } else {
                    mBinder = new IMusicProgressControlImpl();
                    return mBinder;
                }
            default:
                return null;
        }
    }
}
