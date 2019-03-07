package com.zspirytus.enjoymusic.impl.binder;

import android.os.RemoteException;

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
    public EqualizerMetaData addEqualizerSupport() throws RemoteException {
        return EqualizerController.attachToMediaPlayer();
    }

    @Override
    public void setBandLevel(int band, int level) throws RemoteException {
        EqualizerController.setBandLevel((short) band, (short) level);
    }

    @Override
    public boolean isAcousticEchoCancelerAvailable() throws RemoteException {
        return AcousticEchoCanceler.isAcousticEchoCancelerAvailable();
    }

    @Override
    public void setAcousticEchoCancelerEnable(boolean enable) throws RemoteException {
        AcousticEchoCanceler.setAcousticEchoCancelerEnable(enable);
    }

    @Override
    public boolean isAutomaticGainControlAvailable() throws RemoteException {
        return AutomaticGainControl.isAutomaticGainControlAvailable();
    }

    @Override
    public void setAutomaticGainControlEnable(boolean enable) throws RemoteException {
        AutomaticGainControl.setAutomaticGainControlEnable(enable);
    }

    @Override
    public boolean isNoiseSuppressorAvailable() throws RemoteException {
        return NoiseSuppressor.isNoiseSuppressorAvailable();
    }

    @Override
    public void setNoiseSuppressorEnable(boolean enable) throws RemoteException {
        NoiseSuppressor.setNoiseSuppressorEnable(enable);
    }

    @Override
    public void setBassBoostStrength(int strength) throws RemoteException {
        BassBoostHelper.setStrength((short) strength);
    }

    @Override
    public List<String> getPresetReverbNameList() throws RemoteException {
        return PresetReverbHelper.getPresetReverbNameList();
    }

    @Override
    public int[] usePresetReverb(int position) throws RemoteException {
        return PresetReverbHelper.getInstance().usePresetReverb(position);
    }
}
