package com.jd.core.network.gson;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.jd.core.exception.NetworkException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;


/**
 * @author jd
 */
public class BaseResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    BaseResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource buffer = Okio.buffer(value.source());
        String jsonString = buffer.readUtf8();
        try {
            JSONObject object = new JSONObject(jsonString);
            int errorCode = object.getInt("code");
            if (errorCode == 0) {
                String errorMsg = object.getString("errorMsg");
                //异常处理
                throw new NetworkException(errorMsg, errorCode);
            }
            String data = object.getString("data");
            if (data == null || "".equals(data)) {
                return null;
            }
            return adapter.fromJson(data);
        } catch (JSONException e) {
            e.printStackTrace();
            //数据解析异常
            throw new NetworkException(NetworkException.PARSE_ERROR_MSG, NetworkException.PARSE_ERROR);
        } finally {
            value.close();
        }
    }
}