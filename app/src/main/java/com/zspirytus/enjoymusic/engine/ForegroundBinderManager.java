package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.basesdk.utils.LogUtil;
import com.zspirytus.enjoymusic.IBackgroundEventProcessor;
import com.zspirytus.enjoymusic.IBinderPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.AudioFieldObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.FrequencyObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.MusicDeleteObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.NewAudioFileObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayHistoryObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayListObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayMusicObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayStateObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.ProgressObserverManager;

public class ForegroundBinderManager {

    private static class SingletonHolder {
        static ForegroundBinderManager INSTANCE = new ForegroundBinderManager();
    }

    private IBinderPool mBinderPool;

    public static ForegroundBinderManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(IBinderPool binderPool, IBinder.DeathRecipient deathRecipient) {
        mBinderPool = binderPool;
        try {
            mBinderPool.asBinder().linkToDeath(deathRecipient, 0);
        } catch (RemoteException e) {
            LogUtil.log(MainApplication.getAppContext(), "catch_ex.log", e);
        }
    }

    public void registerEvent() {
        try {
            initGlobalData();
            IBinder iBinder = mBinderPool.queryBinder(Constant.BinderCode.BACKGROUND_EVENT_PROCESSOR);
            IBackgroundEventProcessor backgroundEventProcessor = IBackgroundEventProcessor.Stub.asInterface(iBinder);
            backgroundEventProcessor.registerObserver(PlayStateObserverManager.getInstance(), Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER);
            backgroundEventProcessor.registerObserver(ProgressObserverManager.getInstance(), Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER);
            backgroundEventProcessor.registerObserver(PlayMusicObserverManager.getInstance(), Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER);
            backgroundEventProcessor.registerObserver(PlayListObserverManager.getInstance(), Constant.BinderCode.PLAY_LIST_OBSERVER);
            backgroundEventProcessor.registerObserver(PlayHistoryObserverManager.getInstance(), Constant.BinderCode.PLAY_HISTORY_OBSERVER);
            backgroundEventProcessor.registerObserver(AudioFieldObserverManager.getInstance(), Constant.BinderCode.AUDIO_FIELD_OBSERVER);
            backgroundEventProcessor.registerObserver(FrequencyObserverManager.getInstance(), Constant.BinderCode.FREQUENCY_OBSERVER);
            backgroundEventProcessor.registerObserver(MusicDeleteObserverManager.getInstance(), Constant.BinderCode.MUSIC_DELETE_OBSERVER);
            backgroundEventProcessor.registerObserver(NewAudioFileObserverManager.getInstance(), Constant.BinderCode.NEW_AUDIO_FILE_OBSERVER);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void initGlobalData() {
        AudioEffectController controller = AudioEffectController.getInstance();
        controller.isAcousticEchoCancelerAvailable();
        controller.isAutomaticGainControlAvailable();
        controller.isNoiseSuppressorAvailable();
        controller.getPresetReverbNameList();
    }

    public IBinder getBinderByBinderCode(int binderCode) {
        try {
            return mBinderPool.queryBinder(binderCode);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void releaseDead(IBinder.DeathRecipient deathRecipient) {
        if (mBinderPool != null) {
            mBinderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            mBinderPool = null;
        }
    }

}
