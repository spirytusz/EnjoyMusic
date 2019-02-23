
package com.zspirytus.enjoymusic.online.entity.response;

import com.google.gson.annotations.SerializedName;
import com.zspirytus.enjoymusic.online.entity.LiteOnlineSongListItem;

import java.util.List;

@SuppressWarnings("unused")
public class ObtainSongListResponse {

    @SerializedName("code")
    private Long code;
    @SerializedName("data")
    private List<LiteOnlineSongListItem> data;
    @SerializedName("result")
    private String result;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ObtainSongListResponse{" +
                "code=" + code +
                ", data=" + data +
                ", result='" + result + '\'' +
                '}';
    }

    public List<LiteOnlineSongListItem> getData() {
        return data;
    }

    public void setData(List<LiteOnlineSongListItem> data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
