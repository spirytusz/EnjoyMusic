package com.zspirytus.enjoymusic.entity.errorcode;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZSpirytus on 2018/9/10.
 */

@SuppressWarnings("unused")
public class ErrorCode {

    @SerializedName("ErrCode")
    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
