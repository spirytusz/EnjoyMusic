package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.CallSuper;

import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Folder;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;

import java.util.List;

public class MusicDataViewModel extends ViewModel {
    private MutableLiveData<List<Music>> mMusicListLiveData;
    private MutableLiveData<List<Album>> mAlbumListLiveData;
    private MutableLiveData<List<Artist>> mArtistListLiveData;
    private MutableLiveData<List<Folder>> mFolderListLiveData;
    private MutableLiveData<List<SongList>> mSongListLiveData;
    private MutableLiveData<List<Music>> mPlayListLiveData;

    private MutableLiveData<Music> mCurrentMusicLiveData;
    private MutableLiveData<Boolean> mPlayState;

    @CallSuper
    public void init() {
        mMusicListLiveData = new MutableLiveData<>();
        mAlbumListLiveData = new MutableLiveData<>();
        mArtistListLiveData = new MutableLiveData<>();
        mFolderListLiveData = new MutableLiveData<>();
        mPlayListLiveData = new MutableLiveData<>();
        mCurrentMusicLiveData = new MutableLiveData<>();
        mPlayState = new MutableLiveData<>();
        mSongListLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Music>> getMusicList() {
        return mMusicListLiveData;
    }

    public void setMusicList(List<Music> musicList) {
        mMusicListLiveData.setValue(musicList);
    }

    public MutableLiveData<List<Album>> getAlbumList() {
        return mAlbumListLiveData;
    }

    public void setAlbumList(List<Album> albumList) {
        mAlbumListLiveData.setValue(albumList);
    }

    public MutableLiveData<List<Artist>> getArtistList() {
        return mArtistListLiveData;
    }

    public void setArtistList(List<Artist> artistList) {
        mArtistListLiveData.setValue(artistList);
    }

    public MutableLiveData<List<Folder>> getFolderList() {
        return mFolderListLiveData;
    }

    public void setFolderList(List<Folder> folderList) {
        mFolderListLiveData.setValue(folderList);
    }

    public MutableLiveData<Music> getCurrentPlayingMusic() {
        return mCurrentMusicLiveData;
    }

    public void setCurrentPlayingMusic(Music currentMusic) {
        mCurrentMusicLiveData.setValue(currentMusic);
    }

    public MutableLiveData<Boolean> getMusicPlayState() {
        return mPlayState;
    }

    public void setMusicPlayState(boolean isPlaying) {
        mPlayState.setValue(isPlaying);
    }

    public MutableLiveData<List<Music>> getPlayList() {
        return mPlayListLiveData;
    }

    public void setPlayList(List<Music> playList) {
        mPlayListLiveData.setValue(playList);
    }

    public MutableLiveData<List<SongList>> getSongList() {
        return mSongListLiveData;
    }

    public void setSongList(List<SongList> songList) {
        mSongListLiveData.setValue(songList);
    }
}
