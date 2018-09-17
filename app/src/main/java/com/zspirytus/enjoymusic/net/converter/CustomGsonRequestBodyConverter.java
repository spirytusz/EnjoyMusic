package com.zspirytus.enjoymusic.net.converter;

import com.google.gson.Gson;
import com.zspirytus.enjoymusic.entity.requestbody.Body;
import com.zspirytus.enjoymusic.entity.requestbody.SearchMusicByKeyRequestBody;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by ZSpirytus on 2018/9/6.
 */

final class CustomGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    private final Gson gson;

    CustomGsonRequestBodyConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        String jsonString = getJsonString(value.toString());
        return RequestBody.create(MEDIA_TYPE, jsonString);
    }

    private String getJsonString(String musicName) {
        Body body = new Body();
        body.setKey(musicName);
        SearchMusicByKeyRequestBody requestBody = new SearchMusicByKeyRequestBody();
        requestBody.setTransCode("020116");
        requestBody.setOpenId("Test");
        requestBody.setBody(body);
        return gson.toJson(requestBody);
    }
}