package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.entity.listitem.AudioEffectItem;
import com.zspirytus.enjoymusic.entity.listitem.SettingItem;
import com.zspirytus.enjoymusic.global.AudioEffectConfig;

import java.util.ArrayList;
import java.util.List;

public class SettingFragmentViewModel extends ViewModel {

    private static final String TAG = "SettingFragmentViewMode";

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public List<SettingItem> obtainListItem() {
        List<SettingItem> settingItems = new ArrayList<>();
        SettingItem item = new SettingItem();
        item.setTitle(true);
        item.setTitle("音效");
        settingItems.add(item);

        if (AudioEffectConfig.isIsAcousticEchoCancelerAvailable()) {
            item = new SettingItem();
            AudioEffectItem audioEffectItem = new AudioEffectItem();
            audioEffectItem.setTitle("回声消除");
            audioEffectItem.setSingleEffect(true);
            audioEffectItem.setChecked(false);
            item.setAudioEffectItem(audioEffectItem);
            item.setAudioEffect(true);
            settingItems.add(item);
        }
        if (AudioEffectConfig.isIsAutomaticGainControlAvailable()) {
            item = new SettingItem();
            AudioEffectItem audioEffectItem = new AudioEffectItem();
            audioEffectItem.setTitle("自动增益");
            audioEffectItem.setSingleEffect(true);
            audioEffectItem.setChecked(false);
            item.setAudioEffectItem(audioEffectItem);
            item.setAudioEffect(true);
            settingItems.add(item);
        }
        if (AudioEffectConfig.isIsNoiseSuppressorAvailable()) {
            item = new SettingItem();
            AudioEffectItem audioEffectItem = new AudioEffectItem();
            audioEffectItem.setTitle("噪声抑制");
            audioEffectItem.setSingleEffect(true);
            audioEffectItem.setChecked(false);
            item.setAudioEffectItem(audioEffectItem);
            item.setAudioEffect(true);
            settingItems.add(item);
        }

        item = new SettingItem();
        AudioEffectItem audioEffectItem = new AudioEffectItem();
        audioEffectItem.setTitle("音场");
        audioEffectItem.setSingleEffect(false);
        item.setAudioEffectItem(audioEffectItem);
        item.setAudioEffect(true);
        settingItems.add(item);

        item = new SettingItem();
        audioEffectItem = new AudioEffectItem();
        audioEffectItem.setTitle("均衡器");
        audioEffectItem.setSingleEffect(false);
        item.setAudioEffectItem(audioEffectItem);
        item.setAudioEffect(true);
        settingItems.add(item);

        /*item = new SettingItem();
        audioEffectItem = new AudioEffectItem();
        audioEffectItem.setTitle("重低音调节器");
        audioEffectItem.setSingleEffect(false);
        item.setAudioEffectItem(audioEffectItem);
        item.setAudioEffect(true);
        settingItems.add(item);*/

        item = new SettingItem();
        item.setDividerLine(true);
        settingItems.add(item);

        return settingItems;
    }
}
