package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.entity.SettingItem;

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
        item.setTitle("音效");
        item.setHasSwitch(false);
        settingItems.add(item);

        return settingItems;
    }
}
