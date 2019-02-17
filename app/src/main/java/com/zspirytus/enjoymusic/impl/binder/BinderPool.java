package com.zspirytus.enjoymusic.impl.binder;

import android.os.IBinder;

import com.zspirytus.enjoymusic.IBinderPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;

public class BinderPool extends IBinderPool.Stub {

    @Override
    public IBinder queryBinder(int binderCode) {
        switch (binderCode) {
            case Constant.BinderCode.MUSIC_CONTROL:
                return MusicController.getInstance();
            case Constant.BinderCode.MUSIC_PROGRESS_CONTROL:
                return MusicProgressControl.getInstance();
            case Constant.BinderCode.GET_MUSIC_LIST:
                return MusicListGetter.getInstance();
            case Constant.BinderCode.SET_PLAY_LIST:
                return PlayListSetter.getInstance();
            case Constant.BinderCode.BACKGROUND_EVENT_PROCESSOR:
                return BackgroundEventProcessor.getInstance();
            case Constant.BinderCode.AUDIO_EFFECT:
                return AudioEffectHelper.getInstance();
        }
        return null;
    }
}
