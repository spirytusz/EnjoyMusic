package com.zspirytus.enjoymusic.net;

import com.zspirytus.enjoymusic.entity.songinfo.SongInfo;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ZSpirytus on 2018/9/5.
 */

public interface OnlineMusicProvider {

    String BASE_URL = "https://api.hibai.cn/";


    interface Search {
        @POST("api/index/index/")
        Observable<SongInfo> searchMusicByKey(@Body String musicName);
    }
}
