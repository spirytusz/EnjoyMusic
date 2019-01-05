package com.zspirytus.enjoymusic.cache;

import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.listeners.observable.PlayListChangeObservable;

import java.util.ArrayList;
import java.util.List;

public class ForegroundMusicStateCache extends PlayListChangeObservable {

    private static final ForegroundMusicStateCache INSTANCE = new ForegroundMusicStateCache();

    private Music mCurrentPlayingMusic;
    private int mPlayMode;

    private List<Music> mAllMusicList;
    private List<Album> mAlbumList;
    private List<Artist> mArtistList;
    private List<FolderSortedMusic> mFolderSortedMusicList;

    private List<Music> mPlayList = new ArrayList<>();

    private ForegroundMusicStateCache() {
        mCurrentPlayingMusic = MusicSharedPreferences.restoreMusic(MainApplication.getForegroundContext());
        mAllMusicList = new ArrayList<>();
        mAllMusicList = new ArrayList<>();
        mArtistList = new ArrayList<>();
        mFolderSortedMusicList = new ArrayList<>();
    }

    public static ForegroundMusicStateCache getInstance() {
        return INSTANCE;
    }

    public Music getCurrentPlayingMusic() {
        return mCurrentPlayingMusic;
    }

    public void setCurrentPlayingMusic(Music currentPlayingMusic) {
        mCurrentPlayingMusic = currentPlayingMusic;
    }

    public List<Music> getAllMusicList() {
        return mAllMusicList;
    }

    public void setAllMusicList(List<Music> allMusicList) {
        mAllMusicList = allMusicList;
    }

    public List<Album> getAlbumList() {
        return mAlbumList;
    }

    public void setAlbumList(List<Album> albumList) {
        mAlbumList = albumList;
    }

    public List<Artist> getArtistList() {
        return mArtistList;
    }

    public void setArtistList(List<Artist> artistList) {
        mArtistList = artistList;
    }

    public List<Music> getPlayList() {
        return mPlayList;
    }

    public void setPlayList(List<Music> playList) {
        mPlayList = playList;
        notifyAllObserverPlayListChange(playList);
    }

    public List<FolderSortedMusic> getFolderSortedMusicList() {
        return mFolderSortedMusicList;
    }

    public void setFolderSortedMusicList(List<FolderSortedMusic> folderSortedMusicList) {
        mFolderSortedMusicList = folderSortedMusicList;
    }

    public int getPlayMode() {
        return mPlayMode;
    }

    public void setPlayMode(int playMode) {
        mPlayMode = playMode;
    }
}
