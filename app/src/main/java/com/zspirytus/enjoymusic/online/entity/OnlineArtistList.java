
package com.zspirytus.enjoymusic.online.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class OnlineArtistList {

    @SerializedName("artistCount")
    private Long mArtistCount;
    @SerializedName("artists")
    private List<OnlineArtist> mOnlineArtists;

    public Long getArtistCount() {
        return mArtistCount;
    }

    public void setArtistCount(Long artistCount) {
        mArtistCount = artistCount;
    }

    public List<OnlineArtist> getArtists() {
        return mOnlineArtists;
    }

    public void setArtists(List<OnlineArtist> onlineArtists) {
        mOnlineArtists = onlineArtists;
    }

}
