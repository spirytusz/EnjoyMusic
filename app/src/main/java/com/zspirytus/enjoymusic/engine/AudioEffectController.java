package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IAudioEffectHelper;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

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

    public void isAcousticEchoCancelerAvailable(OnResultListener l) {
        ThreadPool.execute(() -> {
            initBinder();
            boolean available = false;
            try {
                available = mAudioEffectHelper.isAcousticEchoCancelerAvailable();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            final boolean result = available;
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                if (l != null) {
                    l.onResult(result);
                }
            });
        });
    }

    public void isAutomaticGainControlAvailable(OnResultListener l) {
        ThreadPool.execute(() -> {
            initBinder();
            boolean available = false;
            try {
                available = mAudioEffectHelper.isAutomaticGainControlAvailable();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            final boolean result = available;
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                if (l != null) {
                    l.onResult(result);
                }
            });
        });
    }

    public void isNoiseSuppressorAvailable(OnResultListener l) {
        ThreadPool.execute(() -> {
            initBinder();
            boolean available = false;
            try {
                available = mAudioEffectHelper.isNoiseSuppressorAvailable();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            final boolean result = available;
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                if (l != null) {
                    l.onResult(result);
                }
            });
        });
    }

    public void getPresetReverbNameList(OnResultListener l) {
        ThreadPool.execute(() -> {
            initBinder();
            List<String> nameList = new ArrayList<>();
            try {
                nameList = mAudioEffectHelper.getPresetReverbNameList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            final List<String> result = nameList;
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                if (l != null) {
                    l.onResult(result);
                }
            });
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

    public void usePresetReverb(OnResultListener l, int position) {
        ThreadPool.execute(() -> {
            initBinder();
            int[] bandLevel = new int[0];
            try {
                bandLevel = mAudioEffectHelper.usePresetReverb(position);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            final int[] result = bandLevel;
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                l.onResult(result);
            });
        });
    }

    public interface OnResultListener {
        void onResult(Object result);
    }

}
