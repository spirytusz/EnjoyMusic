package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;

import com.zspirytus.enjoymusic.entity.SettingItem;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.global.SettingConfig;

import java.util.ArrayList;
import java.util.List;

public class SettingFragmentViewModel extends ViewModel {

    private static final String TAG = "SettingFragmentViewMode";

    private static final String NET_WORK_SOURCE_ENABLE = "networkSourceEnable";

    @Override
    protected void onCleared() {
        super.onCleared();
        saveConfig();
    }

    public List<SettingItem> obtainListItem() {
        List<SettingItem> settingItems = new ArrayList<>();
        SettingItem item = new SettingItem();
        item.setTitle("音效");
        item.setHasSwitch(false);
        settingItems.add(item);

        item = new SettingItem();
        item.setTitle("使用网络源");
        item.setHasSwitch(true);
        item.setChecked(getConfig());
        settingItems.add(item);

        return settingItems;
    }

    private void saveConfig() {
        SharedPreferences.Editor editor = MainApplication.getForegroundContext().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        editor.putBoolean(NET_WORK_SOURCE_ENABLE, SettingConfig.isNetWorkSourceEnable);
        editor.apply();
    }

    private boolean getConfig() {
        SharedPreferences pref = MainApplication.getForegroundContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return pref.getBoolean(NET_WORK_SOURCE_ENABLE, false);
    }
}
