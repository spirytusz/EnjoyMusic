package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IBackgroundEventProcessor;
import com.zspirytus.enjoymusic.IBinderPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.global.AudioEffectConfig;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayHistoryObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayListObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayMusicObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayStateObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.ProgressObserverManager;

import java.util.List;

public class ForegroundBinderManager implements AudioEffectController.OnResultListener {

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
            backgroundEventProcessor.registerObserver(PlayStateObserverManager.getInstance(), Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER);
            backgroundEventProcessor.registerObserver(ProgressObserverManager.getInstance(), Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER);
            backgroundEventProcessor.registerObserver(PlayMusicObserverManager.getInstance(), Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER);
            backgroundEventProcessor.registerObserver(PlayListObserverManager.getInstance(), Constant.BinderCode.PLAY_LIST_OBSERVER);
            backgroundEventProcessor.registerObserver(PlayHistoryObserverManager.getInstance(), Constant.BinderCode.PLAY_HISTORY_OBSERVER);
            initGlobalData();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void initGlobalData() {
        AudioEffectController controller = AudioEffectController.getInstance();
        controller.isAcousticEchoCancelerAvailable(this, 0);
        controller.isAutomaticGainControlAvailable(this, 1);
        controller.isNoiseSuppressorAvailable(this, 2);
        controller.getPresetReverbNameList(this, 3);
    }

    public IBinder getBinderByBinderCode(int binderCode) {
        try {
            return mBinderPool.queryBinder(binderCode);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResult(Object result, int callbackId) {
        switch (callbackId) {
            case 0:
                AudioEffectConfig.setIsAcousticEchoCancelerAvailable((boolean) result);
                break;
            case 1:
                AudioEffectConfig.setIsAutomaticGainControlAvailable((boolean) result);
                break;
            case 2:
                AudioEffectConfig.setIsNoiseSuppressorAvailable((boolean) result);
                break;
            case 3:
                AudioEffectConfig.setPresetReverbNameList((List<String>) result);
                break;
        }
    }

    public void release() {
        try {
            IBinder iBinder = mBinderPool.queryBinder(Constant.BinderCode.BACKGROUND_EVENT_PROCESSOR);
            IBackgroundEventProcessor backgroundEventProcessor = IBackgroundEventProcessor.Stub.asInterface(iBinder);
            backgroundEventProcessor.unregisterObserver(PlayStateObserverManager.getInstance(), Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER);
            backgroundEventProcessor.unregisterObserver(ProgressObserverManager.getInstance(), Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER);
            backgroundEventProcessor.unregisterObserver(PlayMusicObserverManager.getInstance(), Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER);
            backgroundEventProcessor.unregisterObserver(PlayListObserverManager.getInstance(), Constant.BinderCode.PLAY_LIST_OBSERVER);
            backgroundEventProcessor.unregisterObserver(PlayHistoryObserverManager.getInstance(), Constant.BinderCode.PLAY_HISTORY_OBSERVER);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBinderPool = null;
        SingletonHolder.INSTANCE = null;
    }

}
