package com.zspirytus.enjoymusic.impl.binder;

import android.os.IBinder;

import com.zspirytus.enjoymusic.IBinderPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;

public class IBinderPoolImpl extends IBinderPool.Stub {

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
            case Constant.BinderCode.BACKGROUND_EVENT_PROCESSOR:
                return IBackgroundEventProcessorImpl.getInstance();
        }
        return null;
    }
}
