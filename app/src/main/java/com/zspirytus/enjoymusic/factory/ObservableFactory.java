package com.zspirytus.enjoymusic.factory;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZSpirytus on 2018/9/17.
 */

public class ObservableFactory {

    public static Observable<List<Music>> filterMusic(List<Music> musicList, final MusicFilter filter) {
        return Observable.create((ObservableOnSubscribe<List<Music>>) emitter -> {
            emitter.onNext(filter.filter(musicList));
            emitter.onComplete();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

}
