package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.entity.SettingItem;

import java.util.ArrayList;
import java.util.List;

public class SettingFragmentViewModel extends ViewModel {

    public List<SettingItem> obtainListItem() {
        List<SettingItem> settingItems = new ArrayList<>();
        SettingItem item = new SettingItem();
        item.setTitle("音效");
        item.setHasSwitch(false);
        settingItems.add(item);

        item = new SettingItem();
        item.setTitle("使用网络源");
        item.setHasSwitch(true);
        settingItems.add(item);

        return settingItems;
    }
}
