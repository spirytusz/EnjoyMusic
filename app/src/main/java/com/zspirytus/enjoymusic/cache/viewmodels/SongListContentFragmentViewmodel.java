package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;

import java.util.List;

public class SongListContentFragmentViewmodel extends ViewModel {

    public List<Music> getMusicListFromgSongList(SongList songList) {
        return songList.getSongsOfThisSongList();
    }

    public SpannableString createSpannableString(String title, List<Music> musicList) {
        long totalDuration = 0;
        for (Music music : musicList) {
            totalDuration += music.getMusicDuration();
        }
        String countOfMusic = musicList.size() + "首曲目";
        long durationMinsValue = totalDuration / 1000 / 60;
        String duration;
        if (durationMinsValue > 60) {
            long min = durationMinsValue % 60;
            long hour = durationMinsValue / 60;
            duration = hour + "小时" + min + "分钟";
        } else {
            duration = durationMinsValue + "分钟";
        }
        String content = title + "\n" + countOfMusic + "\n" + duration;
        SpannableString spannableString = new SpannableString(content);
        int pointer = 0;
        spannableString.setSpan(new RelativeSizeSpan(1.6f), 0, title.length(), 0);

        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), 0);
        pointer += title.length() + 1;

        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), pointer, pointer + countOfMusic.length(), 0);
        pointer += countOfMusic.length() + 1;

        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), pointer, pointer + duration.length(), 0);
        return spannableString;
    }
}
