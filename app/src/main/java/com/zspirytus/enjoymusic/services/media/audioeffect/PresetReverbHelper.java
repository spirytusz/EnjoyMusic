package com.zspirytus.enjoymusic.services.media.audioeffect;

import android.media.audiofx.PresetReverb;

import com.zspirytus.enjoymusic.services.media.MediaPlayController;

import java.util.List;

// 预设音场控制器
public class PresetReverbHelper {

    private static PresetReverb mPresetReverb;

    public static int[] usePresetReverb(int position) {
        if (mPresetReverb == null) {
            int audioSessionId = MediaPlayController.getInstance().getAudioSessionId();
            mPresetReverb = new PresetReverb(0, audioSessionId);
        }
        return EqualizerController.usePresetReverb(position);
    }

    public static List<String> getPresetReverbNameList() {
        return EqualizerController.getPresetNameList();
    }
}
