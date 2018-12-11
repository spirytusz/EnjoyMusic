package com.zspirytus.enjoymusic.factory;

import android.os.IBinder;

import com.zspirytus.enjoymusic.IGetMusicList;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.entity.HomePageRecyclerViewItem;
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

    public static Observable<HomePageRecyclerViewItem> getHomePageRecyclerViewItemsObservable() {
        return Observable.create(new ObservableOnSubscribe<HomePageRecyclerViewItem>() {
            @Override
            public void subscribe(ObservableEmitter<HomePageRecyclerViewItem> emitter) throws Exception {
                List<Music> musicList = ForegroundMusicCache.getInstance().getAllMusicList();
                Collections.sort(musicList, new Comparator<Music>() {
                    @Override
                    public int compare(Music o1, Music o2) {
                        return (int) (o1.getMusicAddDate() / 1000 - o2.getMusicAddDate() / 1000);
                    }
                });
                emitter.onNext(new HomePageRecyclerViewItem());
                for (Music music : musicList)
                    emitter.onNext(new HomePageRecyclerViewItem(music));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Object> getMusicListInForegroundObservable() {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.GET_MUSIC_LIST);
                IGetMusicList getMusicListBinder = IGetMusicList.Stub.asInterface(binder);
                ForegroundMusicCache.getInstance().setAllMusicList(getMusicListBinder.getMusicList());
                ForegroundMusicCache.getInstance().setAlbumList(getMusicListBinder.getMusicAlbumList());
                ForegroundMusicCache.getInstance().setArtistList(getMusicListBinder.getMusicArtistList());
                ForegroundMusicCache.getInstance().setFolderSortedMusicList(getMusicListBinder.getFolderSortedMusicList());
                emitter.onNext(1);
                emitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

}
