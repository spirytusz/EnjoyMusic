package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.entity.listitem.MusicPickItem;

import java.util.ArrayList;
import java.util.List;

public class MusicPickFragmentViewModel extends ViewModel {

    public List<Music> filterUnSelectedMusic(List<MusicPickItem> itemList) {
        List<Music> musicList = new ArrayList<>();
        for (MusicPickItem item : itemList) {
            if (item.isSelected()) {
                musicList.add(item.getMusic());
            }
        }
        return musicList;
    }
}
