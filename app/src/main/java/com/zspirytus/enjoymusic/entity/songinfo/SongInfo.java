
package com.zspirytus.enjoymusic.entity.songinfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class SongInfo {

    @SerializedName("Body")
    private List<Body> body;
    @SerializedName("ErrCode")
    private String errCode;
    @SerializedName("ResultCode")
    private Long resultCode;

    public List<Body> getBody() {
        return body;
    }

    public void setBody(List<Body> body) {
        this.body = body;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public Long getResultCode() {
        return resultCode;
    }

    public void setResultCode(Long resultCode) {
        this.resultCode = resultCode;
    }

}
