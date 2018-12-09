package com.zspirytus.enjoymusic.factory;

import android.os.IBinder;

import com.zspirytus.enjoymusic.IGetMusicList;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.entity.Music;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZSpirytus on 2018/9/17.
 */

public class ObservableFactory {

    public static Observable<List<Music>> getHomePageRecyclerViewItemsObservable() {
        return Observable.create(new ObservableOnSubscribe<List<Music>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Music>> emitter) throws Exception {
                List<Music> musicList = ForegroundMusicCache.getInstance().getAllMusicList();
                Collections.sort(musicList, new Comparator<Music>() {
                    @Override
                    public int compare(Music o1, Music o2) {
                        return (int) (o1.getMusicAddDate() / 1000 - o2.getMusicAddDate() / 1000);
                    }
                });
                emitter.onNext(musicList);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /*public static Observable<Music> getMusicObservableConverterByTypeAndKey(final String key, int type) {
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
    }*/

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
