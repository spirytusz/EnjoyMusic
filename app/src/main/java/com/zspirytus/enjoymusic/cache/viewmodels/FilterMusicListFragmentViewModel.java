package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.db.table.jointable.JoinMusicToSongList;

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
        long totalDuration = 0;
        for (Music music : musicList) {
            totalDuration += music.getMusicDuration();
        }
        String countOfMusic = musicList.size() + "首曲目";
        String duration = (totalDuration / 1000 / 60) + "分钟";
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
        switch (flag) {
            case ALBUM_FLAG:
                return "专辑";
            case ARTIST_FLAG:
                return "艺术家";
            case FOLDER_FLAG:
                return "文件夹";
            case SONG_LIST_FLAG:
                return "歌单";
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
