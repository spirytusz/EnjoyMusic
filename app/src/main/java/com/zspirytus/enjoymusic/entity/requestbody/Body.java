
package com.zspirytus.enjoymusic.entity.requestbody;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Body {

    @Expose
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
