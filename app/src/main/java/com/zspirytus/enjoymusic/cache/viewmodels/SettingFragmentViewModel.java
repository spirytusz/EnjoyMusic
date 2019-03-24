package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.content.res.Resources;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.engine.AudioEffectController;
import com.zspirytus.enjoymusic.entity.listitem.AudioEffectItem;
import com.zspirytus.enjoymusic.entity.listitem.SettingItem;
import com.zspirytus.enjoymusic.global.AudioEffectConfig;
import com.zspirytus.enjoymusic.global.MainApplication;

import java.util.ArrayList;
import java.util.List;

public class SettingFragmentViewModel extends ViewModel {

    private static final String TAG = "SettingFragmentViewMode";

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public List<SettingItem> obtainListItem() {
        Resources resources = MainApplication.getAppContext().getResources();
        List<SettingItem> settingItems = new ArrayList<>();
        SettingItem item = new SettingItem();
        item.setTitle(true);
        item.setTitle(resources.getString(R.string.audio_effect));
        settingItems.add(item);

        if (AudioEffectConfig.isIsAcousticEchoCancelerAvailable()) {
            item = new SettingItem();
            AudioEffectItem audioEffectItem = new AudioEffectItem();
            audioEffectItem.setTitle(resources.getString(R.string.audio_effect_acoustic_echo_canceler));
            audioEffectItem.setSingleEffect(true);
            audioEffectItem.setFlag(0);
            audioEffectItem.setChecked(AudioEffectConfig.isAcousticEchoCancelerEnable());
            item.setAudioEffectItem(audioEffectItem);
            item.setAudioEffect(true);
            settingItems.add(item);
        }
        if (AudioEffectConfig.isIsAutomaticGainControlAvailable()) {
            item = new SettingItem();
            AudioEffectItem audioEffectItem = new AudioEffectItem();
            audioEffectItem.setTitle(resources.getString(R.string.audio_effect_automatic_gain_control));
            audioEffectItem.setSingleEffect(true);
            audioEffectItem.setFlag(1);
            audioEffectItem.setChecked(AudioEffectConfig.isAutomaticGainControlEnable());
            item.setAudioEffectItem(audioEffectItem);
            item.setAudioEffect(true);
            settingItems.add(item);
        }
        if (AudioEffectConfig.isIsNoiseSuppressorAvailable()) {
            item = new SettingItem();
            AudioEffectItem audioEffectItem = new AudioEffectItem();
            audioEffectItem.setTitle(resources.getString(R.string.audio_effect_noise_suppressor));
            audioEffectItem.setSingleEffect(true);
            audioEffectItem.setFlag(2);
            audioEffectItem.setChecked(AudioEffectConfig.isNoiseSuppressorEnable());
            item.setAudioEffectItem(audioEffectItem);
            item.setAudioEffect(true);
            settingItems.add(item);
        }

        item = new SettingItem();
        AudioEffectItem audioEffectItem = new AudioEffectItem();
        audioEffectItem.setTitle(resources.getString(R.string.audio_effect_audio_field));
        audioEffectItem.setSingleEffect(false);
        item.setAudioEffectItem(audioEffectItem);
        item.setAudioEffect(true);
        settingItems.add(item);

        item = new SettingItem();
        audioEffectItem = new AudioEffectItem();
        audioEffectItem.setTitle(resources.getString(R.string.audio_effect_equalizer));
        audioEffectItem.setSingleEffect(false);
        item.setAudioEffectItem(audioEffectItem);
        item.setAudioEffect(true);
        settingItems.add(item);

        /*item = new SettingItem();
        audioEffectItem = new AudioEffectItem();
        audioEffectItem.setTitle(resources.getString(R.string.audio_effect_bass_boast));
        audioEffectItem.setSingleEffect(false);
        item.setAudioEffectItem(audioEffectItem);
        item.setAudioEffect(true);
        settingItems.add(item);*/

        item = new SettingItem();
        item.setDividerLine(true);
        settingItems.add(item);

        return settingItems;
    }

    public void setSingleEffect(AudioEffectItem audioEffectItem) {
        switch (audioEffectItem.getFlag()) {
            case 0:
                AudioEffectController.getInstance().setAcousticEchoCancelerEnable(audioEffectItem.isChecked());
                break;
            case 1:
                AudioEffectController.getInstance().setAutomaticGainControlEnable(audioEffectItem.isChecked());
                break;
            case 2:
                AudioEffectController.getInstance().setNoiseSuppressorEnable(audioEffectItem.isChecked());
                break;
        }
    }
}
