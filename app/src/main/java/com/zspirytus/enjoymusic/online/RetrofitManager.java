package com.zspirytus.enjoymusic.online;

import com.zspirytus.enjoymusic.online.entity.SearchResultMusic;
import com.zspirytus.enjoymusic.online.method.SearchMusic;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static class Singleton {
        static RetrofitManager INSTANCE = new RetrofitManager();
    }

    private static final String BASE_URL = "https://api.bzqll.comb/music/tencent/";

    private Retrofit mRetrofit;

    private RetrofitManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
    }

    private static Retrofit getRetrofit() {
        return Singleton.INSTANCE.mRetrofit;
    }

    public static void searchMusic(String s, Observer<List<SearchResultMusic>> observer) {
        getRetrofit().create(SearchMusic.class)
                .get(s, "579621905", "song").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
