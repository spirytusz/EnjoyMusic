package com.zspirytus.enjoymusic.online;

import com.zspirytus.basesdk.utils.LogUtil;
import com.zspirytus.basesdk.utils.ToastUtil;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.online.entity.response.ObtainSongListResponse;
import com.zspirytus.enjoymusic.online.entity.response.SearchAlbumResponse;
import com.zspirytus.enjoymusic.online.entity.response.SearchArtistResponse;
import com.zspirytus.enjoymusic.online.entity.response.SearchMusicResponse;
import com.zspirytus.enjoymusic.online.entity.response.SearchSongListResponse;
import com.zspirytus.enjoymusic.online.methods.ObtainMethod;
import com.zspirytus.enjoymusic.online.methods.SearchMethod;
import com.zspirytus.enjoymusic.utils.NetWorkUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static class Singleton {
        static RetrofitManager INSTANCE = new RetrofitManager();
    }

    private static final String BASE_URL = "https://api.bzqll.com/";

    private Retrofit mRetrofit;
    private SearchMethod mSearchMethod;
    private ObtainMethod mObtainMethod;

    private RetrofitManager() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> {
            //打印retrofit日志
            LogUtil.e("RetrofitLog", "retrofitBack = " + message);
        });

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    }

    private static Retrofit getRetrofit() {
        return Singleton.INSTANCE.mRetrofit;
    }

    private static SearchMethod getSearchMethod() {
        if (Singleton.INSTANCE.mSearchMethod == null) {
            Singleton.INSTANCE.mSearchMethod = getRetrofit().create(SearchMethod.class);
        }
        return Singleton.INSTANCE.mSearchMethod;
    }

    private static ObtainMethod getObtainMethod() {
        if (Singleton.INSTANCE.mObtainMethod == null) {
            Singleton.INSTANCE.mObtainMethod = getRetrofit().create(ObtainMethod.class);
        }
        return Singleton.INSTANCE.mObtainMethod;
    }

    public static void searchMusic(String musicName, Observer<SearchMusicResponse> observer) {
        if (NetWorkUtil.isNetWorkReady()) {
            getSearchMethod().searchMusic(musicName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } else {
            ToastUtil.showToast(MainApplication.getAppContext(), R.string.no_net_work_tip);
        }
    }

    public static void searchAlbum(String albumName, Observer<SearchAlbumResponse> observer) {
        if (NetWorkUtil.isNetWorkReady()) {
            getSearchMethod().searchAlbum(albumName)
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } else {
            ToastUtil.showToast(MainApplication.getAppContext(), R.string.no_net_work_tip);
        }
    }

    public static void searchSongList(String songListId, Observer<SearchSongListResponse> observer) {
        if (NetWorkUtil.isNetWorkReady()) {
            getSearchMethod().searchSongList(songListId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } else {
            ToastUtil.showToast(MainApplication.getAppContext(), R.string.no_net_work_tip);
        }
    }

    public static void searchArtist(String artistName, Observer<SearchArtistResponse> observer) {
        if (NetWorkUtil.isNetWorkReady()) {
            getSearchMethod().searchArtist(artistName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } else {
            ToastUtil.showToast(MainApplication.getAppContext(), R.string.no_net_work_tip);
        }
    }

    public static void obtainHotSongList(Observer<ObtainSongListResponse> observer) {
        if (NetWorkUtil.isNetWorkReady()) {
            getObtainMethod().obtainHotSongList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } else {
            ToastUtil.showToast(MainApplication.getAppContext(), R.string.no_net_work_tip);
        }
    }

}
