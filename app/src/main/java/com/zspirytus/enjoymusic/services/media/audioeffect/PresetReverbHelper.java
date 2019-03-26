package com.zspirytus.enjoymusic.services.media.audioeffect;

import android.media.audiofx.PresetReverb;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.cache.AudioConfigSharedPreferences;
import com.zspirytus.enjoymusic.foregroundobserver.IAudioFieldChangeObserver;
import com.zspirytus.enjoymusic.listeners.observable.PresetReverbObservable;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

import java.util.List;

// 预设音场控制器
public class PresetReverbHelper extends PresetReverbObservable {

    private static class Singleton {
        static PresetReverbHelper INSTANCE = new PresetReverbHelper();
    }

    private PresetReverbHelper() {
        usePresetReverb(AudioConfigSharedPreferences.restoreAudioField());
    }

    @Override
    public void register(IAudioFieldChangeObserver observer) {
        mRemoteCallbackList.register(observer);
    }

    @Override
    public void unregister(IAudioFieldChangeObserver observer) {
        mRemoteCallbackList.unregister(observer);
    }

    @Override
    public void notifyAllObserverAudioFieldChange(int position) {
        int size = mRemoteCallbackList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mRemoteCallbackList.getBroadcastItem(i).onChange(position);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mRemoteCallbackList.finishBroadcast();
    }

    public static PresetReverbHelper getInstance() {
        return Singleton.INSTANCE;
    }

    private PresetReverb mPresetReverb;

    public void usePresetReverb(int position) {
        if (mPresetReverb == null) {
            int audioSessionId = MediaPlayController.getInstance().getAudioSessionId();
            mPresetReverb = new PresetReverb(0, audioSessionId);
        }
        AudioConfigSharedPreferences.saveAudioField(position);
        notifyAllObserverAudioFieldChange(position);
        EqualizerController.usePresetReverb(position);
    }

    public static List<String> getPresetReverbNameList() {
        return EqualizerController.getPresetNameList();
    }
}
