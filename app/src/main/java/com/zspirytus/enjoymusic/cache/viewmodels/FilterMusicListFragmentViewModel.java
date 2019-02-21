package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.JoinSongListToSong;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class FilterMusicListFragmentViewModel extends ViewModel {

    private static final String EXTRA_KEY = "extra";
    private static final String FILTER_EXTRA_KEY = "filterExtra";
    private static final String MUSIC_LIST_EXTRA_KEY = "MusicListExtra";

    private MutableLiveData<String> mExtra;
    private MutableLiveData<List<Music>> mMusicListExtra;

    public void init() {
        mExtra = new MutableLiveData<>();
        mMusicListExtra = new MutableLiveData<>();
    }

    public MutableLiveData<String> getTitle() {
        return mExtra;
    }

    public MutableLiveData<List<Music>> getMusicList() {
        return mMusicListExtra;
    }

    public SongList createNewSongList(String songListName, List<Music> musicList) {
        SongList songList = new SongList();
        songList.setMusicCount(musicList.size());
        songList.setSongListName(songListName);
        songList.setSongListId(System.currentTimeMillis());
        List<JoinSongListToSong> joinSongListToSongs = new ArrayList<>();
        for (Music music : musicList) {
            JoinSongListToSong joinSongListToSong = new JoinSongListToSong();
            joinSongListToSong.setSongId(music.getId());
            joinSongListToSong.setSongListId(songList.getSongListId());
            joinSongListToSongs.add(joinSongListToSong);
        }
        DBManager.getInstance().getDaoSession().getSongListDao().insert(songList);
        DBManager.getInstance().getDaoSession().getJoinSongListToSongDao().insertInTx(joinSongListToSongs);
        return songList;
    }

    public void obtainExtra(Bundle bundle, List<Music> musics) {
        List<Music> musicList = bundle.getParcelableArrayList(MUSIC_LIST_EXTRA_KEY);
        if (musicList != null && !musicList.isEmpty()) {
            mMusicListExtra.setValue(musicList);
        } else {
            applyMusicListByFilter(bundle, musics);
        }
        String extra = bundle.getString(EXTRA_KEY);
        if (extra != null) {
            mExtra.setValue(extra);
        }
    }

    private void applyMusicListByFilter(Bundle bundle, List<Music> musicList) {
        MusicFilter filter = bundle.getParcelable(FILTER_EXTRA_KEY);
        mExtra.setValue(!filter.getAlbum().isEmpty() ? "专辑" : "艺术家");
        ThreadPool.execute(() -> {
            List<Music> musics = filter.filter(musicList);
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                mMusicListExtra.setValue(musics);
            });
        });
    }
}
