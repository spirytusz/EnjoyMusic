package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.db.table.Album;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Example local unit include_main_activity, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Observable.create(new ObservableOnSubscribe<Album>() {
            @Override
            public void subscribe(ObservableEmitter<Album> emitter) throws Exception {

            }
        }).subscribe(new Observer<Album>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Album album) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}