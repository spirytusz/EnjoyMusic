package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.engine.AudioEffectController;
import com.zspirytus.enjoymusic.entity.EqualizerMetaData;
import com.zspirytus.enjoymusic.entity.listitem.AudioEffectItem;
import com.zspirytus.enjoymusic.global.AudioEffectConfig;
import com.zspirytus.enjoymusic.view.fragment.AudioEffectFragment;

import java.util.ArrayList;
import java.util.List;

public class AudioEffectViewModel extends ViewModel {

    private static final String TAG = "AudioEffectViewModel";

    private MutableLiveData<List<AudioEffectItem>> mPresetReverbNameList = new MutableLiveData<>();
    private MutableLiveData<EqualizerMetaData> mEqualizerMetaData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mBassBoastFlag = new MutableLiveData<>();

    public MutableLiveData<List<AudioEffectItem>> getPresetReverbNameList() {
        return mPresetReverbNameList;
    }

    public MutableLiveData<EqualizerMetaData> getEqualizerMetaData() {
        return mEqualizerMetaData;
    }

    public MutableLiveData<Boolean> getBassBoastFlag() {
        return mBassBoastFlag;
    }

    public void obtainDataByFlag(int flag) {
        switch (flag) {
            case AudioEffectFragment.FLAG_AUDIO_FILED:
                obtainPresetReverbList();
                break;
            case AudioEffectFragment.FLAG_EQUALIZER:
                obtainEqualizerMetaData();
                break;
            case AudioEffectFragment.FLAG_BASS_BOAST:
                mBassBoastFlag.setValue(true);
                break;
        }
    }

    private void obtainPresetReverbList() {
        List<String> nameList = AudioEffectConfig.getPresetReverbNameList();
        List<AudioEffectItem> audioEffectItems = new ArrayList<>();
        for (String name : nameList) {
            AudioEffectItem item = new AudioEffectItem();
            item.setTitle(name);
            item.setChecked(false);
            item.setSingleEffect(true);
            audioEffectItems.add(item);
        }
        mPresetReverbNameList.setValue(audioEffectItems);
    }

    private void obtainEqualizerMetaData() {
        AudioEffectController.getInstance().attachEqualizer((result, callbackId) -> {
            if (result != null) {
                mEqualizerMetaData.setValue((EqualizerMetaData) result);
            }
        });
    }

    public void setPresetReverb(int position) {
        AudioEffectController.getInstance().usePresetReverb((result, callbackId) -> {
        }, position, 0);
    }

    public void setBandLevel(int band, int level) {
        AudioEffectController.getInstance().setBandLevel((short) band, (short) level);
    }

    public void setBassBoastStrength(int strength) {
        AudioEffectController.getInstance().setBassBoastStrength(strength);
    }
}
