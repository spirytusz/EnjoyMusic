package com.zspirytus.enjoymusic.online.methods;

import com.zspirytus.enjoymusic.online.entity.response.ObtainSongListResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ObtainMethod {

    @GET("music/tencent/hotSongList?key=579621905&sortId=3&limit=10")
    Observable<ObtainSongListResponse> obtainHotSongList();
}
