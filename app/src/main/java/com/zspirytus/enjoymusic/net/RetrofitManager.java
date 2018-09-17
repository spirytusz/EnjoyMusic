package com.zspirytus.enjoymusic.net;

import com.zspirytus.enjoymusic.entity.songinfo.SongInfo;
import com.zspirytus.enjoymusic.net.converter.CustomGsonConverterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by ZSpirytus on 2018/9/5.
 */

public class RetrofitManager {

    private static final int TIME_OUT = 10;

    private Retrofit mRetrofit;

    private static RetrofitManager INSTANCE;

    private RetrofitManager() {
        OkHttpClient client = getClient();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(OnlineMusicProvider.BASE_URL)
                .client(client)
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    public static RetrofitManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitManager();
        }
        return INSTANCE;
    }

    public Observable<SongInfo> searchMusicByKey(String musicName) {
        return mRetrofit.create(OnlineMusicProvider.Search.class).searchMusicByKey(musicName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

}
