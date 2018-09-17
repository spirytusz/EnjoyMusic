package com.zspirytus.enjoymusic.net.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.zspirytus.enjoymusic.entity.errorcode.ErrorCode;
import com.zspirytus.enjoymusic.net.exception.ApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

/**
 * Created by ZSpirytus on 2018/9/6.
 */

final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        ErrorCode errorCode = gson.fromJson(response, ErrorCode.class);
        try {
            check(errorCode);
        } catch (ApiException e) {
            value.close();
        }

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }

    private void check(ErrorCode errorCode) {
        if (errorCode != null) {
            String errorCodeString = errorCode.getErrorCode();
            switch (errorCodeString) {
                case "0000":
                    throw new ApiException(errorCodeString, "OpenID不存在");
                case "9999":
                    throw new ApiException(errorCodeString, "type或id不存在");
                case "0001":
                    throw new ApiException(errorCodeString, "交易码不存在");
                case "0002":
                    throw new ApiException(errorCodeString, "非法请求或非POST请求");
                case "0003":
                    throw new ApiException(errorCodeString, "模块不存在");
                case "0004":
                    throw new ApiException(errorCodeString, "控制器不存在");
                case "0005":
                    throw new ApiException(errorCodeString, "方法不存在");
                case "1001":
                    throw new ApiException(errorCodeString, "网易云歌单ID不存在");
                case "1002":
                    throw new ApiException(errorCodeString, "网易云音乐ID不存在");
                case "1003":
                    throw new ApiException(errorCodeString, "网易云音乐歌单不存在");
                case "1004":
                    throw new ApiException(errorCodeString, "网易云音乐搜索关键词不存在");
                case "2001":
                    throw new ApiException(errorCodeString, "酷狗音乐歌单ID不存在");
                case "2002":
                    throw new ApiException(errorCodeString, "酷狗音乐音乐ID不存在");
                case "2003":
                    throw new ApiException(errorCodeString, "酷狗音乐歌单不存在");
                case "2004":
                    throw new ApiException(errorCodeString, "酷狗音乐搜索关键词不存在");
                case "3001":
                    throw new ApiException(errorCodeString, "QQ音乐歌单ID不存在");
                case "3002":
                    throw new ApiException(errorCodeString, "QQ音乐音乐ID不存在");
                case "3003":
                    throw new ApiException(errorCodeString, "QQ音乐歌单不存在");
                case "3004":
                    throw new ApiException(errorCodeString, "QQ音乐搜索关键词不存在");
                case "4001":
                    throw new ApiException(errorCodeString, "搜索关键词不存在");
            }
        }
    }
}
