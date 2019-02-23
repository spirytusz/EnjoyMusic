
package com.zspirytus.enjoymusic.online.entity.response;

import com.google.gson.annotations.SerializedName;
import com.zspirytus.enjoymusic.online.entity.OnlineSongList;

@SuppressWarnings("unused")
public class SearchSongListResponse {

    @SerializedName("code")
    private Long mCode;
    @SerializedName("data")
    private OnlineSongList mData;
    @SerializedName("result")
    private String mResult;

    public Long getCode() {
        return mCode;
    }

    public void setCode(Long code) {
        mCode = code;
    }

    public OnlineSongList getData() {
        return mData;
    }

    public void setData(OnlineSongList data) {
        mData = data;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }

    @Override
    public String toString() {
        return "SearchSongListResponse{" +
                "mCode=" + mCode +
                ", mData=" + mData +
                ", mResult='" + mResult + '\'' +
                '}';
    }
}
