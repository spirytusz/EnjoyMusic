package com.zspirytus.enjoymusic.net.exception;

/**
 * Created by ZSpirytus on 2018/9/7.
 */

public class ApiException extends IllegalArgumentException {

    String mErrorCode;

    public ApiException(String errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }

    public String getErrorCode() {
        return mErrorCode;
    }

}
