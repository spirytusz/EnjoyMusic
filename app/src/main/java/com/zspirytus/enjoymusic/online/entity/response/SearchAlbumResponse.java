
package com.zspirytus.enjoymusic.online.entity.response;

import com.google.gson.annotations.Expose;
import com.zspirytus.enjoymusic.online.entity.OnlineAlbum;

import java.util.List;

@SuppressWarnings("unused")
public class SearchAlbumResponse {

    @Expose
    private Long code;
    @Expose
    private List<OnlineAlbum> data;
    @Expose
    private String result;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public List<OnlineAlbum> getData() {
        return data;
    }

    public void setData(List<OnlineAlbum> data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
