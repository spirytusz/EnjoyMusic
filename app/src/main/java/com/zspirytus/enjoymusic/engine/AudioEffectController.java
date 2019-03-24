package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.basesdk.thread.UIThreadSwitcher;
import com.zspirytus.enjoymusic.IAudioEffectHelper;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.EqualizerMetaData;
import com.zspirytus.enjoymusic.global.AudioEffectConfig;

import java.util.ArrayList;
import java.util.List;

public class AudioEffectController {

    private IAudioEffectHelper mAudioEffectHelper;

    private static class Singleton {
        static AudioEffectController INSTANCE = new AudioEffectController();
    }

    private AudioEffectController() {
    }

    public static AudioEffectController getInstance() {
        return Singleton.INSTANCE;
    }

    private void initBinder() {
        if (mAudioEffectHelper == null) {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            mAudioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
        }
    }

    public void setBandLevel(short band, short level) {
        ThreadPool.execute(() -> {
            initBinder();
            try {
                mAudioEffectHelper.setBandLevel((int) band, (int) level);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void isAcousticEchoCancelerAvailable() {
        ThreadPool.execute(() -> {
            initBinder();
            boolean available = false;
            try {
                available = mAudioEffectHelper.isAcousticEchoCancelerAvailable();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            AudioEffectConfig.setIsAcousticEchoCancelerAvailable(available);
        });
    }

    public void isAutomaticGainControlAvailable() {
        ThreadPool.execute(() -> {
            initBinder();
            boolean available = false;
            try {
                available = mAudioEffectHelper.isAutomaticGainControlAvailable();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            AudioEffectConfig.setIsAutomaticGainControlAvailable(available);
        });
    }

    public void isNoiseSuppressorAvailable() {
        ThreadPool.execute(() -> {
            initBinder();
            boolean available = false;
            try {
                available = mAudioEffectHelper.isNoiseSuppressorAvailable();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            AudioEffectConfig.setIsNoiseSuppressorAvailable(available);
        });
    }

    public void getPresetReverbNameList() {
        ThreadPool.execute(() -> {
            initBinder();
            List<String> nameList = new ArrayList<>();
            try {
                nameList = mAudioEffectHelper.getPresetReverbNameList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            AudioEffectConfig.setPresetReverbNameList(nameList);
        });
    }

    public void setAcousticEchoCancelerEnable(boolean enable) {
        ThreadPool.execute(() -> {
            initBinder();
            try {
                mAudioEffectHelper.setAcousticEchoCancelerEnable(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void setAutomaticGainControlEnable(boolean enable) {
        ThreadPool.execute(() -> {
            initBinder();
            try {
                mAudioEffectHelper.setAutomaticGainControlEnable(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void setNoiseSuppressorEnable(boolean enable) {
        ThreadPool.execute(() -> {
            initBinder();
            try {
                mAudioEffectHelper.setNoiseSuppressorEnable(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void setBassBoostStrength(int strength) {
        ThreadPool.execute(() -> {
            initBinder();
            try {
                mAudioEffectHelper.setBassBoostStrength(strength);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void usePresetReverb(int position) {
        ThreadPool.execute(() -> {
            initBinder();
            try {
                mAudioEffectHelper.usePresetReverb(position);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void attachEqualizer(OnFetchEqualizerMetaData l) {
        ThreadPool.execute(() -> {
            initBinder();
            EqualizerMetaData metaData = null;
            try {
                metaData = mAudioEffectHelper.addEqualizerSupport();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            final EqualizerMetaData result = metaData;
            UIThreadSwitcher.runOnMainThreadSync(() -> l.onResult(result));
        });
    }

    public void setBassBoastStrength(int strength) {
        ThreadPool.execute(() -> {
            initBinder();
            try {
                mAudioEffectHelper.setBassBoostStrength(strength);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public interface OnFetchEqualizerMetaData {
        void onResult(EqualizerMetaData equalizerMetaData);
    }

}
