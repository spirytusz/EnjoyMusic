package com.zspirytus.enjoymusic.services.media.audioeffect;

import android.media.audiofx.PresetReverb;

import com.zspirytus.enjoymusic.cache.MusicSharedPreferences;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.listeners.observable.PresetReverbObservable;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

import java.util.List;

// 预设音场控制器
public class PresetReverbHelper extends PresetReverbObservable {

    private static class Singleton {
        static PresetReverbHelper INSTANCE = new PresetReverbHelper();
    }

    private PresetReverbHelper() {
        usePresetReverb(MusicSharedPreferences.restoreAudioField(MainApplication.getBackgroundContext()));
    }

    public static PresetReverbHelper getInstance() {
        return Singleton.INSTANCE;
    }

    private PresetReverb mPresetReverb;

    public int[] usePresetReverb(int position) {
        if (mPresetReverb == null) {
            int audioSessionId = MediaPlayController.getInstance().getAudioSessionId();
            mPresetReverb = new PresetReverb(0, audioSessionId);
        }
        MusicSharedPreferences.saveAudioField(position);
        notifyAllObserverAudioFieldChange(position);
        return EqualizerController.usePresetReverb(position);
    }

    public static List<String> getPresetReverbNameList() {
        return EqualizerController.getPresetNameList();
    }
}
