package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.engine.AudioEffectController;
import com.zspirytus.enjoymusic.entity.AudioEffectItem;
import com.zspirytus.enjoymusic.entity.EqualizerMetaData;
import com.zspirytus.enjoymusic.global.AudioEffectConfig;

import java.util.ArrayList;
import java.util.List;

public class AudioEffectViewModel extends ViewModel {

    private MutableLiveData<List<AudioEffectItem>> mAudioEffects = new MutableLiveData<>();
    private MutableLiveData<List<AudioEffectItem>> mPresetReverbNameList = new MutableLiveData<>();
    private MutableLiveData<EqualizerMetaData> mEqualizerMetaData = new MutableLiveData<>();

    private boolean isEqualizerInited = false;

    public MutableLiveData<List<AudioEffectItem>> getAudioEffects() {
        return mAudioEffects;
    }

    public MutableLiveData<List<AudioEffectItem>> getPresetReverbNameList() {
        return mPresetReverbNameList;
    }

    public MutableLiveData<EqualizerMetaData> getEqualizerMetaData() {
        return mEqualizerMetaData;
    }

    public boolean isEqualizerInited() {
        return isEqualizerInited;
    }

    public void obtainAudioEffects() {
        List<AudioEffectItem> audioEffects = new ArrayList<>();
        AudioEffectItem item = new AudioEffectItem();
        if (AudioEffectConfig.isIsAcousticEchoCancelerAvailable()) {
            item.setTitle("回声消除");
            item.setSingleEffect(true);
            item.setChecked(false);
            audioEffects.add(item);
        }
        if (AudioEffectConfig.isIsAutomaticGainControlAvailable()) {
            item = new AudioEffectItem();
            item.setTitle("自动增益");
            item.setSingleEffect(true);
            item.setChecked(false);
            audioEffects.add(item);
        }
        if (AudioEffectConfig.isIsNoiseSuppressorAvailable()) {
            item = new AudioEffectItem();
            item.setTitle("噪声抑制");
            item.setSingleEffect(true);
            item.setChecked(false);
            audioEffects.add(item);
        }
        item = new AudioEffectItem();
        item.setTitle("音场...");
        item.setSingleEffect(false);
        item.setChecked(false);
        audioEffects.add(item);
        mAudioEffects.setValue(audioEffects);
    }

    public void obtainPresetReverbList() {
        List<String> nameList = AudioEffectConfig.getPresetReverbNameList();
        List<AudioEffectItem> audioEffectItems = new ArrayList<>();
        for (String name : nameList) {
            AudioEffectItem item = new AudioEffectItem();
            item.setTitle(name);
            item.setChecked(false);
            item.setSingleEffect(true);
            audioEffectItems.add(item);
        }
        AudioEffectItem equalizerItem = new AudioEffectItem();
        equalizerItem.setTitle("自定义");
        equalizerItem.setChecked(false);
        equalizerItem.setSingleEffect(false);
        audioEffectItems.add(equalizerItem);
        audioEffectItems.get(0).setChecked(true);
        mPresetReverbNameList.setValue(audioEffectItems);
    }

    public void obtainEqualizerMetaData() {
        AudioEffectController.getInstance().attachEqualizer((result, callbackId) -> {
            if (result != null) {
                isEqualizerInited = true;
                mEqualizerMetaData.setValue((EqualizerMetaData) result);
            }
        });
    }

    public void setEffectEnable(boolean enable, int position) {
        String effectName = getAudioEffects().getValue().get(position).getTitle();
        switch (effectName) {
            case "回声消除":
                AudioEffectController.getInstance().setAcousticEchoCancelerEnable(enable);
                break;
            case "自动增益":
                AudioEffectController.getInstance().setAutomaticGainControlEnable(enable);
                break;
            case "噪声抑制":
                AudioEffectController.getInstance().setNoiseSuppressorEnable(enable);
                break;
        }
    }

    public void setPresetReverb(int position) {
        AudioEffectController.getInstance().usePresetReverb((result, callbackId) -> {
        }, position, 0);
    }

    public void setBandLevel(int band, int level) {
        AudioEffectController.getInstance().setBandLevel((short) band, (short) level);
    }
}
