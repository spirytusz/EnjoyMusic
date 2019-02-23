
package com.zspirytus.enjoymusic.online.entity.response;

import com.google.gson.annotations.Expose;
import com.zspirytus.enjoymusic.online.entity.OnlineArtistList;

@SuppressWarnings("unused")
public class SearchArtistResponse {

    @Expose
    private Long code;
    @Expose
    private OnlineArtistList data;
    @Expose
    private String result;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public OnlineArtistList getData() {
        return data;
    }

    public void setData(OnlineArtistList data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
