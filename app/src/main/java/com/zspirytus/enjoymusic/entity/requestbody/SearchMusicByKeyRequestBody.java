
package com.zspirytus.enjoymusic.entity.requestbody;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SearchMusicByKeyRequestBody {

    @SerializedName("Body")
    private Body body;
    @SerializedName("OpenId")
    private String openId;
    @SerializedName("TransCode")
    private String transCode;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

}
