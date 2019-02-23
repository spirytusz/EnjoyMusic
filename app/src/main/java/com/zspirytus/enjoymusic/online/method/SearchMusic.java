package com.zspirytus.enjoymusic.online.method;

import com.zspirytus.enjoymusic.online.entity.SearchResultMusic;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchMusic {

    @GET("search")
    Observable<List<SearchResultMusic>> get(@Query("s") String s, @Query("key") String key, @Query("type") String type);
}
