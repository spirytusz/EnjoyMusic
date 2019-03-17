package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.db.table.jointable.JoinMusicToSongList;
import com.zspirytus.enjoymusic.global.MainApplication;

import java.util.ArrayList;
import java.util.List;

public class FilterMusicListFragmentViewModel extends ViewModel {

    private static final int PREVIEW_TITLE_MAX_LENGTH_EN = 16;

    private static final int ALBUM_FLAG = 1;
    private static final int ARTIST_FLAG = 2;
    private static final int FOLDER_FLAG = 3;
    private static final int SONG_LIST_FLAG = 4;

    public SongList createNewSongList(String songListName, List<Music> musicList) {
        SongList songList = new SongList();
        songList.setMusicCount(musicList.size());
        songList.setSongListName(songListName);
        songList.setSongListId(System.currentTimeMillis());
        List<JoinMusicToSongList> joinSongListToSongs = new ArrayList<>();
        for (Music music : musicList) {
            JoinMusicToSongList joinMusicToSongList = new JoinMusicToSongList();
            joinMusicToSongList.setMusicId(music.getMusicId());
            joinMusicToSongList.setSongListId(songList.getSongListId());
            joinSongListToSongs.add(joinMusicToSongList);
        }
        DBManager.getInstance().getDaoSession().getSongListDao().insert(songList);
        DBManager.getInstance().getDaoSession().getJoinMusicToSongListDao().insertInTx(joinSongListToSongs);
        return songList;
    }

    public SpannableString createSpannableString(String title, List<Music> musicList) {
        Resources resources = MainApplication.getAppContext().getResources();
        long totalDuration = 0;
        for (Music music : musicList) {
            totalDuration += music.getMusicDuration();
        }
        String countOfMusic = musicList.size() + resources.getString(R.string._number_of_music);
        long durationMinsValue = totalDuration / 1000 / 60;
        String duration;
        if (durationMinsValue > 60) {
            long min = durationMinsValue % 60;
            long hour = durationMinsValue / 60;
            duration = hour + resources.getString(R.string._hour) + min + resources.getString(R.string._min);
        } else {
            duration = durationMinsValue + resources.getString(R.string._min);
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

    public String getToolbarTitle(int flag) {
        Resources resources = MainApplication.getAppContext().getResources();
        switch (flag) {
            case ALBUM_FLAG:
                return resources.getString(R.string.album);
            case ARTIST_FLAG:
                return resources.getString(R.string.artist);
            case FOLDER_FLAG:
                return resources.getString(R.string.folder);
            case SONG_LIST_FLAG:
                return resources.getString(R.string.song_list);
        }
        return "";
    }

    public String getPreviewTitle(String title) {
        int len = 0;
        StringBuilder builder = new StringBuilder();
        char[] charArray = title.toCharArray();
        for (Character c : charArray) {
            if (c < 256) {
                len++;
            } else {
                len += 2;
            }
            if (len >= PREVIEW_TITLE_MAX_LENGTH_EN) {
                break;
            }
            builder.append(c);
        }
        if (len >= PREVIEW_TITLE_MAX_LENGTH_EN) {
            builder.append("…");
        }
        return builder.toString();
    }
}
