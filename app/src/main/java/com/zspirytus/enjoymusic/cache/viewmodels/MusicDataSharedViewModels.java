package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.entity.Music;

import java.util.List;

public class MusicDataSharedViewModels extends ViewModel {

    private MutableLiveData<List<Music>> mMusicListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Album>> mAlbumListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Artist>> mArtistListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<FolderSortedMusic>> mFolderListLiveData = new MutableLiveData<>();
    private MutableLiveData<Music> mCurrentMusicLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mPlayState = new MutableLiveData<>();

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

    public MutableLiveData<List<FolderSortedMusic>> getFolderList() {
        return mFolderListLiveData;
    }

    public void setFolderList(List<FolderSortedMusic> folderList) {
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
}
