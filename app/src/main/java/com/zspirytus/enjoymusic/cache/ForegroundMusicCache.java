package com.zspirytus.enjoymusic.cache;

import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.listeners.observable.PlayListChangeObservable;

import java.util.List;

public class ForegroundMusicCache extends PlayListChangeObservable {

    private static final ForegroundMusicCache INSTANCE = new ForegroundMusicCache();

    private Music mCurrentPlayingMusic;

    private List<Music> mAllMusicList;
    private List<Album> mAlbumList;
    private List<Artist> mArtistList;

    private List<Music> mPlayList;

    private ForegroundMusicCache() {
    }

    public static ForegroundMusicCache getInstance() {
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
}
