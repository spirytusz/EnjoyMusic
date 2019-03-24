package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.cache.AudioConfigSharedPreferences;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.engine.AudioEffectController;
import com.zspirytus.enjoymusic.entity.EqualizerMetaData;
import com.zspirytus.enjoymusic.entity.listitem.AudioEffectItem;
import com.zspirytus.enjoymusic.global.AudioEffectConfig;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.AudioFieldObserverManager;
import com.zspirytus.enjoymusic.receivers.observer.AudioFieldChangeObserver;
import com.zspirytus.enjoymusic.view.fragment.AudioEffectFragment;

import java.util.ArrayList;
import java.util.List;

public class AudioEffectViewModel extends ViewModel implements AudioFieldChangeObserver {

    private static final String TAG = "AudioEffectViewModel";

    private MutableLiveData<List<AudioEffectItem>> mPresetReverbNameList = new MutableLiveData<>();
    private MutableLiveData<EqualizerMetaData> mEqualizerMetaData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mBassBoastFlag = new MutableLiveData<>();
    private MutableLiveData<Integer> mSelectAudioField = new MutableLiveData<>();

    public MutableLiveData<List<AudioEffectItem>> getPresetReverbNameList() {
        return mPresetReverbNameList;
    }

    public MutableLiveData<EqualizerMetaData> getEqualizerMetaData() {
        return mEqualizerMetaData;
    }

    public MutableLiveData<Boolean> getBassBoastFlag() {
        return mBassBoastFlag;
    }

    public MutableLiveData<Integer> getSelectAudioField() {
        return mSelectAudioField;
    }

    public void applyDataByFlag(int flag) {
        ThreadPool.execute(() -> {
            switch (flag) {
                case AudioEffectFragment.FLAG_AUDIO_FILED:
                    applyPresetReverbList();
                    break;
                case AudioEffectFragment.FLAG_EQUALIZER:
                    obtainEqualizerMetaData();
                    break;
                case AudioEffectFragment.FLAG_BASS_BOAST:
                    mBassBoastFlag.postValue(true);
                    break;
            }
        });
    }

    private void applyPresetReverbList() {
        AudioFieldObserverManager.getInstance().register(this);
        ThreadPool.execute(() -> {
            List<String> nameList = AudioEffectConfig.getPresetReverbNameList();
            List<AudioEffectItem> audioEffectItems = new ArrayList<>();
            int selectPosition = AudioConfigSharedPreferences.restoreAudioField();
            for (int i = 0; i < nameList.size(); i++) {
                AudioEffectItem item = new AudioEffectItem();
                item.setTitle(nameList.get(i));
                item.setChecked(i == selectPosition);
                item.setSingleEffect(true);
                audioEffectItems.add(item);
            }
            mPresetReverbNameList.postValue(audioEffectItems);
        });
    }

    private void obtainEqualizerMetaData() {
        AudioEffectController.getInstance().attachEqualizer(equalizerMetaData -> {
            if (equalizerMetaData != null) {
                mEqualizerMetaData.postValue(equalizerMetaData);
            }
        });
    }

    public void setPresetReverb(int position) {
        AudioEffectController.getInstance().usePresetReverb(position);
    }

    public void setBandLevel(int band, int level) {
        AudioEffectController.getInstance().setBandLevel((short) band, (short) level);
    }

    public void setBassBoastStrength(int strength) {
        AudioEffectController.getInstance().setBassBoastStrength(strength);
    }

    @Override
    public void onChange(int position) {
        mSelectAudioField.postValue(position);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        AudioFieldObserverManager.getInstance().unregister(this);
    }
}
