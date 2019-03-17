package com.zspirytus.enjoymusic.impl.binder;

import com.zspirytus.enjoymusic.IAudioEffectHelper;
import com.zspirytus.enjoymusic.entity.EqualizerMetaData;
import com.zspirytus.enjoymusic.services.media.audioeffect.AcousticEchoCanceler;
import com.zspirytus.enjoymusic.services.media.audioeffect.AutomaticGainControl;
import com.zspirytus.enjoymusic.services.media.audioeffect.BassBoostHelper;
import com.zspirytus.enjoymusic.services.media.audioeffect.EqualizerController;
import com.zspirytus.enjoymusic.services.media.audioeffect.NoiseSuppressor;
import com.zspirytus.enjoymusic.services.media.audioeffect.PresetReverbHelper;

import java.util.List;

public class AudioEffectHelper extends IAudioEffectHelper.Stub {

    private static class Singleton {
        static AudioEffectHelper INSTANCE = new AudioEffectHelper();
    }

    private AudioEffectHelper() {
    }

    public static AudioEffectHelper getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public EqualizerMetaData addEqualizerSupport() {
        return EqualizerController.attachToMediaPlayer();
    }

    @Override
    public void setBandLevel(int band, int level) {
        EqualizerController.setBandLevel((short) band, (short) level);
    }

    @Override
    public boolean isAcousticEchoCancelerAvailable() {
        return AcousticEchoCanceler.isAcousticEchoCancelerAvailable();
    }

    @Override
    public void setAcousticEchoCancelerEnable(boolean enable) {
        AcousticEchoCanceler.setAcousticEchoCancelerEnable(enable);
    }

    @Override
    public boolean isAutomaticGainControlAvailable() {
        return AutomaticGainControl.isAutomaticGainControlAvailable();
    }

    @Override
    public void setAutomaticGainControlEnable(boolean enable) {
        AutomaticGainControl.setAutomaticGainControlEnable(enable);
    }

    @Override
    public boolean isNoiseSuppressorAvailable() {
        return NoiseSuppressor.isNoiseSuppressorAvailable();
    }

    @Override
    public void setNoiseSuppressorEnable(boolean enable) {
        NoiseSuppressor.setNoiseSuppressorEnable(enable);
    }

    @Override
    public void setBassBoostStrength(int strength) {
        BassBoostHelper.setStrength((short) strength);
    }

    @Override
    public List<String> getPresetReverbNameList() {
        return PresetReverbHelper.getPresetReverbNameList();
    }

    @Override
    public int[] usePresetReverb(int position) {
        return PresetReverbHelper.getInstance().usePresetReverb(position);
    }
}
