package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.basesdk.utils.ToastUtil;
import com.zspirytus.enjoymusic.IGetMusicList;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Folder;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.db.table.jointable.JoinMusicToSongList;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.MusicDeleteObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.NewAudioFileObserverManager;
import com.zspirytus.enjoymusic.receivers.observer.MusicDeleteObserver;
import com.zspirytus.enjoymusic.receivers.observer.NewAudioFileObserver;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends MusicPlayingStateViewModel implements MusicDeleteObserver, NewAudioFileObserver {

    private MutableLiveData<Music> mNewMusic;
    private MutableLiveData<Album> mNewAlbum;
    private MutableLiveData<Artist> mNewArtist;

    @Override
    public void init() {
        super.init();
        mNewMusic = new MutableLiveData<>();
        mNewAlbum = new MutableLiveData<>();
        mNewArtist = new MutableLiveData<>();

        MusicDeleteObserverManager.getInstance().register(this);
        NewAudioFileObserverManager.getInstance().register(this);
    }

    @Override
    public void onMusicDelete(Music music) {
        List<Music> musicList = getMusicList().getValue();
        if (musicList != null) {
            musicList.remove(music);
        }
        getMusicList().postValue(musicList);
    }

    @Override
    public void onNewMusic(Music music) {
        mNewMusic.postValue(music);
    }

    @Override
    public void onNewAlbum(Album album) {
        mNewAlbum.postValue(album);
    }

    @Override
    public void onNewArtist(Artist artist) {
        mNewArtist.postValue(artist);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        MusicDeleteObserverManager.getInstance().unregister(this);
        NewAudioFileObserverManager.getInstance().unregister(this);
    }

    public MutableLiveData<Music> getNewMusic() {
        return mNewMusic;
    }

    public MutableLiveData<Album> getNewAlbum() {
        return mNewAlbum;
    }

    public MutableLiveData<Artist> getNewArtist() {
        return mNewArtist;
    }

    public void refreshSongLists(String songListName, List<Music> musicList) {
        if (songListName != null && !songListName.isEmpty()) {
            boolean isDuplicate = isSongListNameDuplicate(songListName);
            if (!isDuplicate) {
                ThreadPool.execute(() -> {
                    // create New SongList Instance
                    SongList songList = new SongList();
                    songList.setMusicCount(musicList.size());
                    songList.setSongListName(songListName);
                    songList.setSongListId(System.currentTimeMillis());

                    // insert into database
                    List<JoinMusicToSongList> joinSongListToSongs = new ArrayList<>();
                    for (Music music : musicList) {
                        JoinMusicToSongList joinMusicToSongList = new JoinMusicToSongList();
                        joinMusicToSongList.setMusicId(music.getMusicId());
                        joinMusicToSongList.setSongListId(songList.getSongListId());
                        joinSongListToSongs.add(joinMusicToSongList);
                    }
                    DBManager.getInstance().getDaoSession().getSongListDao().insert(songList);
                    DBManager.getInstance().getDaoSession().getJoinMusicToSongListDao().insertInTx(joinSongListToSongs);

                    // refresh MainActivityViewModel SongLists.
                    List<SongList> songLists = getSongList().getValue();
                    songLists.add(songList);
                    getSongList().postValue(songLists);

                    ToastUtil.postToShow(MainApplication.getAppContext(), R.string.success);
                });
            } else {
                ToastUtil.showToast(MainApplication.getAppContext(), R.string.duplicate_song_list_name);
            }
        } else {
            ToastUtil.showToast(MainApplication.getAppContext(), R.string.empty_song_list_name);
        }
    }

    public boolean isSongListNameDuplicate(String songListName) {
        List<SongList> songLists = DBManager.getInstance().getDaoSession().loadAll(SongList.class);
        for (SongList songList : songLists) {
            if (songList.getSongListName().equals(songListName)) {
                return true;
            }
        }
        return false;
    }

    public void obtainMusicList() {
        ThreadPool.execute(() -> {
            try {
                IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.GET_MUSIC_LIST);
                IGetMusicList getMusicListBinder = IGetMusicList.Stub.asInterface(binder);
                List<Music> allMusicList = getMusicListBinder.getMusicList();
                List<Album> albumList = getMusicListBinder.getAlbumList();
                List<Artist> artistList = getMusicListBinder.getArtistList();
                List<Folder> folderList = getMusicListBinder.getFolderList();

                getMusicList().postValue(allMusicList);
                getAlbumList().postValue(albumList);
                getArtistList().postValue(artistList);
                getFolderList().postValue(folderList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void obtainEmptyData() {
        getMusicList().setValue(new ArrayList<>());
        getAlbumList().setValue(new ArrayList<>());
        getArtistList().setValue(new ArrayList<>());
        getFolderList().setValue(new ArrayList<>());
    }

    public void applySongLists() {
        List<SongList> songLists = DBManager.getInstance().getDaoSession().getSongListDao().loadAll();
        setSongList(songLists);
    }

    void updateArtist(Artist needUpdateArtist) {
        List<Artist> artistList = getArtistList().getValue();
        if (artistList == null) {
            try {
                IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.GET_MUSIC_LIST);
                IGetMusicList getMusicListBinder = IGetMusicList.Stub.asInterface(binder);
                artistList = getMusicListBinder.getArtistList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < artistList.size(); i++) {
            if (needUpdateArtist.getArtistId().equals(artistList.get(i).getArtistId())) {
                artistList.get(i).setArtistArt(needUpdateArtist.peakArtistArt());
                break;
            }
        }
        getArtistList().postValue(artistList);
    }

    void updateAlbum(Album album) {
        List<Album> albumList = getAlbumList().getValue();
        if (albumList == null) {
            try {
                IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.GET_MUSIC_LIST);
                IGetMusicList getMusicListBinder = IGetMusicList.Stub.asInterface(binder);
                albumList = getMusicListBinder.getAlbumList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < albumList.size(); i++) {
            if (album.getAlbumId().equals(albumList.get(i).getAlbumId())) {
                albumList.set(i, album);
                break;
            }
        }
        getAlbumList().postValue(albumList);
        getMusicList().postValue(getMusicList().getValue());

        // force refresh currentPlayingMusic when its album equal to needUpdateAlbum.
        Music currentPlayMusic = getCurrentPlayingMusic().getValue();
        if (currentPlayMusic != null && currentPlayMusic.getAlbumId().equals(album.getAlbumId())) {
            getCurrentPlayingMusic().postValue(getCurrentPlayingMusic().getValue());
        }
    }
}
