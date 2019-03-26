package com.zspirytus.enjoymusic.services.media.audioeffect;

import android.media.audiofx.PresetReverb;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.cache.AudioConfigSharedPreferences;
import com.zspirytus.enjoymusic.foregroundobserver.IAudioFieldChangeObserver;
import com.zspirytus.enjoymusic.listeners.observable.RemoteObservable;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

import java.util.List;

// 预设音场控制器
public class PresetReverbHelper extends RemoteObservable<IAudioFieldChangeObserver, Integer> {

    private static class Singleton {
        static PresetReverbHelper INSTANCE = new PresetReverbHelper();
    }

    private PresetReverbHelper() {
        usePresetReverb(AudioConfigSharedPreferences.restoreAudioField());
    }

    @Override
    public void register(IAudioFieldChangeObserver observer) {
        getCallbackList().register(observer);
    }

    @Override
    public void unregister(IAudioFieldChangeObserver observer) {
        getCallbackList().unregister(observer);
    }

    @Override
    protected void notifyChange(Integer position) {
        int size = getCallbackList().beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                getCallbackList().getBroadcastItem(i).onChange(position);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        getCallbackList().finishBroadcast();
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
        notifyChange(position);
        EqualizerController.usePresetReverb(position);
    }

    public static List<String> getPresetReverbNameList() {
        return EqualizerController.getPresetNameList();
    }
}
