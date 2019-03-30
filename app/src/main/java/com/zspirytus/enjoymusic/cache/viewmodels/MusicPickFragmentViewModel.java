package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.db.table.jointable.JoinMusicToSongList;
import com.zspirytus.enjoymusic.entity.listitem.MusicPickItem;

import java.util.ArrayList;
import java.util.List;

public class MusicPickFragmentViewModel extends ViewModel {

    public SongList saveSongListToDB(String songListName, List<MusicPickItem> itemList) {
        List<Music> musicList = new ArrayList<>();
        for (MusicPickItem item : itemList) {
            if (item.isSelected()) {
                musicList.add(item.getMusic());
            }
        }
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
        DBManager.getInstance().getDaoSession().getJoinMusicToSongListDao().insertOrReplaceInTx(joinSongListToSongs);
        return songList;
    }
}
