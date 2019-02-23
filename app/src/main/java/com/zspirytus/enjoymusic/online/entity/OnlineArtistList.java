
package com.zspirytus.enjoymusic.online.entity;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class OnlineArtistList {

    @Expose
    private Long artistCount;
    @Expose
    private List<OnlineArtist> artists;

    public Long getArtistCount() {
        return artistCount;
    }

    public void setArtistCount(Long artistCount) {
        this.artistCount = artistCount;
    }

    public List<OnlineArtist> getArtists() {
        return artists;
    }

    public void setArtists(List<OnlineArtist> artists) {
        this.artists = artists;
    }

}
