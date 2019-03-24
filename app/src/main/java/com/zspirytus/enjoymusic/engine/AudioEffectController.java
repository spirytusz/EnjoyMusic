package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.basesdk.thread.UIThreadSwitcher;
import com.zspirytus.basesdk.utils.LogUtil;
import com.zspirytus.enjoymusic.IAudioEffectHelper;
import com.zspirytus.enjoymusic.cache.AudioConfigSharedPreferences;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.EqualizerMetaData;
import com.zspirytus.enjoymusic.global.AudioEffectConfig;

import java.util.List;

public class AudioEffectController {

    private static class Singleton {
        static AudioEffectController INSTANCE = new AudioEffectController();
    }

    private AudioEffectController() {
    }

    public static AudioEffectController getInstance() {
        return Singleton.INSTANCE;
    }

    public void setBandLevel(short band, short level) {
        ThreadPool.execute(() -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            IAudioEffectHelper audioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
            try {
                audioEffectHelper.setBandLevel((int) band, (int) level);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    void isAcousticEchoCancelerAvailable() {
        ThreadPool.execute(() -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            IAudioEffectHelper audioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
            try {
                boolean available = audioEffectHelper.isAcousticEchoCancelerAvailable();
                AudioEffectConfig.setIsAcousticEchoCancelerAvailable(available);
                if (available) {
                    AudioEffectConfig.setIsAcousticEchoCancelerEnable(AudioConfigSharedPreferences.obtainAcousticEchoCancelerEnable());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    void isAutomaticGainControlAvailable() {
        ThreadPool.execute(() -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            IAudioEffectHelper audioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
            try {
                boolean available = audioEffectHelper.isAutomaticGainControlAvailable();
                AudioEffectConfig.setIsAutomaticGainControlAvailable(available);
                if (available) {
                    AudioEffectConfig.setIsAutomaticGainControlEnable(AudioConfigSharedPreferences.obtainAutomaticGainControlEnable());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    void isNoiseSuppressorAvailable() {
        ThreadPool.execute(() -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            IAudioEffectHelper audioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
            try {
                boolean available = audioEffectHelper.isNoiseSuppressorAvailable();
                AudioEffectConfig.setIsNoiseSuppressorAvailable(available);
                if (available) {
                    AudioEffectConfig.setIsNoiseSuppressorEnable(AudioConfigSharedPreferences.obtainNoiseSuppressorEnable());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void getPresetReverbNameList() {
        ThreadPool.execute(() -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            IAudioEffectHelper audioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
            try {
                List<String> nameList = audioEffectHelper.getPresetReverbNameList();
                AudioEffectConfig.setPresetReverbNameList(nameList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void setAcousticEchoCancelerEnable(boolean enable) {
        ThreadPool.execute(() -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            IAudioEffectHelper audioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
            try {
                audioEffectHelper.setAcousticEchoCancelerEnable(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void setAutomaticGainControlEnable(boolean enable) {
        ThreadPool.execute(() -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            IAudioEffectHelper audioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
            try {
                audioEffectHelper.setAutomaticGainControlEnable(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void setNoiseSuppressorEnable(boolean enable) {
        ThreadPool.execute(() -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            IAudioEffectHelper audioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
            try {
                audioEffectHelper.setNoiseSuppressorEnable(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void setBassBoostStrength(int strength) {
        ThreadPool.execute(() -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            IAudioEffectHelper audioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
            try {
                audioEffectHelper.setBassBoostStrength(strength);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void usePresetReverb(int position) {
        LogUtil.e(this.getClass().getSimpleName(), "pos = " + position);
        ThreadPool.execute(() -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            IAudioEffectHelper audioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
            try {
                audioEffectHelper.usePresetReverb(position);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void attachEqualizer(OnFetchEqualizerMetaData l) {
        ThreadPool.execute(() -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            IAudioEffectHelper audioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
            final EqualizerMetaData metaData;
            try {
                metaData = audioEffectHelper.addEqualizerSupport();
                UIThreadSwitcher.runOnMainThreadSync(() -> l.onResult(metaData));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void setBassBoastStrength(int strength) {
        ThreadPool.execute(() -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.AUDIO_EFFECT);
            IAudioEffectHelper audioEffectHelper = IAudioEffectHelper.Stub.asInterface(binder);
            try {
                audioEffectHelper.setBassBoostStrength(strength);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public interface OnFetchEqualizerMetaData {
        void onResult(EqualizerMetaData equalizerMetaData);
    }

}
