package com.zspirytus.enjoymusic.online.methods;

import com.zspirytus.enjoymusic.online.entity.response.SearchAlbumResponse;
import com.zspirytus.enjoymusic.online.entity.response.SearchArtistResponse;
import com.zspirytus.enjoymusic.online.entity.response.SearchMusicResponse;
import com.zspirytus.enjoymusic.online.entity.response.SearchSongListResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchMethod {

    @GET("music/tencent/search?key=579621905&type=song&limit=10")
    Observable<SearchMusicResponse> searchMusic(@Query("s") String musicName);

    @GET("music/tencent/search?key=579621905&type=album&limit=10")
    Observable<SearchAlbumResponse> searchAlbum(@Query("s") String albumName);

    @GET("music/netease/search?key=579621905&type=singer&limit=5")
    Observable<SearchArtistResponse> searchArtist(@Query("s") String artistName);

    @GET("music/tencent/songList?key=579621905&limit=10")
    Observable<SearchSongListResponse> searchSongList(@Query("id") String id);
}
