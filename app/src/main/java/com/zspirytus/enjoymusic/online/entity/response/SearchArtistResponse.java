
package com.zspirytus.enjoymusic.online.entity.response;

import com.google.gson.annotations.SerializedName;
import com.zspirytus.enjoymusic.online.entity.OnlineArtistList;

@SuppressWarnings("unused")
public class SearchArtistResponse {

    @SerializedName("code")
    private Long mCode;
    @SerializedName("data")
    private OnlineArtistList mOnlineArtistList;
    @SerializedName("result")
    private String mResult;

    public Long getCode() {
        return mCode;
    }

    public void setCode(Long code) {
        mCode = code;
    }

    public OnlineArtistList getData() {
        return mOnlineArtistList;
    }

    public void setData(OnlineArtistList onlineArtistList) {
        mOnlineArtistList = onlineArtistList;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }

}
