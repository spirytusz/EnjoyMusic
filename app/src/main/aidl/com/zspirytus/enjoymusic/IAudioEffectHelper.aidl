// IAudioEffectHelper.aidl
package com.zspirytus.enjoymusic;

import java.util.List;

import com.zspirytus.enjoymusic.entity.EqualizerMetaData;

interface IAudioEffectHelper {
    EqualizerMetaData addEqualizerSupport();
    void setBandLevel(int band, int level);
    boolean isAcousticEchoCancelerAvailable();
    void setAcousticEchoCancelerEnable(boolean enable);
    boolean isAutomaticGainControlAvailable();
    void setAutomaticGainControlEnable(boolean enable);
    boolean isNoiseSuppressorAvailable();
    void setNoiseSuppressorEnable(boolean enable);
    void setBassBoostStrength(int strength);
    List<String> getPresetReverbNameList();
    void usePresetReverb(int position);
}
