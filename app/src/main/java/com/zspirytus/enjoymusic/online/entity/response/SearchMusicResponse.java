
package com.zspirytus.enjoymusic.online.entity.response;

import com.google.gson.annotations.SerializedName;
import com.zspirytus.enjoymusic.online.entity.OnlineMusic;

import java.util.List;

@SuppressWarnings("unused")
public class SearchMusicResponse {

    @SerializedName("code")
    private Long mCode;
    @SerializedName("data")
    private List<OnlineMusic> mData;
    @SerializedName("result")
    private String mResult;

    public Long getCode() {
        return mCode;
    }

    public void setCode(Long code) {
        mCode = code;
    }

    public List<OnlineMusic> getData() {
        return mData;
    }

    public void setData(List<OnlineMusic> data) {
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
        return "SearchMusicResponse{" +
                "mCode=" + mCode +
                ", mData=" + mData +
                ", mResult='" + mResult + '\'' +
                '}';
    }
}
