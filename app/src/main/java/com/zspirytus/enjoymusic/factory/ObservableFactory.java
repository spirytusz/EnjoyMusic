package com.zspirytus.enjoymusic.factory;

import android.os.IBinder;

import com.zspirytus.enjoymusic.IGetMusicList;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.HomePageChildRecyclerViewItem;
import com.zspirytus.enjoymusic.entity.HomePageRecyclerViewItem;
import com.zspirytus.enjoymusic.entity.Music;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZSpirytus on 2018/9/17.
 */

public class ObservableFactory {

    public static Observable<List<HomePageRecyclerViewItem>> getHomePageRecyclerViewItemsObservable() {
        return Observable.create(new ObservableOnSubscribe<List<HomePageRecyclerViewItem>>() {
            @Override
            public void subscribe(ObservableEmitter<List<HomePageRecyclerViewItem>> emitter) throws Exception {
                List<Music> musicList = ForegroundMusicCache.getInstance().getAllMusicList();
                List<Album> albumList = ForegroundMusicCache.getInstance().getAlbumList();
                List<Artist> artistList = ForegroundMusicCache.getInstance().getArtistList();
                List<HomePageChildRecyclerViewItem> childOfMusic = new ArrayList<>();
                List<HomePageChildRecyclerViewItem> childOfAlbum = new ArrayList<>();
                List<HomePageChildRecyclerViewItem> childOfArtist = new ArrayList<>();
                for (Music music : musicList) {
                    childOfMusic.add(new HomePageChildRecyclerViewItem(music.getMusicThumbAlbumCoverPath(), music.getMusicName(), music.getMusicArtist()));
                }
                for (Album album : albumList) {
                    childOfAlbum.add(new HomePageChildRecyclerViewItem(album.getAlbumCoverPath(), album.getAlbumName(), album.getArtist()));
                }
                for (Artist artist : artistList) {
                    childOfArtist.add(new HomePageChildRecyclerViewItem(null, artist.getArtistName(), artist.getMusicCount() + " 首曲目"));
                }
                List<HomePageRecyclerViewItem> recyclerViewItems = new ArrayList<>();
                recyclerViewItems.add(new HomePageRecyclerViewItem(Constant.HomePageTabTitle.ALL, childOfMusic));
                recyclerViewItems.add(new HomePageRecyclerViewItem(Constant.HomePageTabTitle.ALBUM, childOfAlbum));
                recyclerViewItems.add(new HomePageRecyclerViewItem(Constant.HomePageTabTitle.ARTIST, childOfArtist));
                emitter.onNext(recyclerViewItems);
                emitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public static Observable<Music> getMusicObservableConverterByTypeAndKey(final String key, int type) {
        Observable<Music> observable = Observable.create(new ObservableOnSubscribe<Music>() {
            @Override
            public void subscribe(ObservableEmitter<Music> emitter) throws Exception {
                List<Music> musicList = ForegroundMusicCache.getInstance().getAllMusicList();
                for (Music music : musicList) {
                    emitter.onNext(music);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        switch (type) {
            case 0:
                return observable;
            case 1:
                return observable.filter(new Predicate<Music>() {
                    @Override
                    public boolean test(Music music) throws Exception {
                        return music.getMusicAlbumName().equals(key);
                    }
                });
            case 2:
                return observable.filter(new Predicate<Music>() {
                    @Override
                    public boolean test(Music music) throws Exception {
                        return music.getMusicArtist().equals(key);
                    }
                });
        }
        return null;
    }

    public static Observable<Object> getMusicListInForegroundObservable() {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.GET_MUSIC_LIST);
                IGetMusicList getMusicListBinder = IGetMusicList.Stub.asInterface(binder);
                emitter.onNext(getMusicListBinder.getMusicList());
                emitter.onNext(getMusicListBinder.getMusicAlbumList());
                emitter.onNext(getMusicListBinder.getMusicArtistList());
                emitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

}
