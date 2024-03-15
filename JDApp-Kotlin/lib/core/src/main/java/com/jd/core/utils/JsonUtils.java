package com.jd.core.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private JsonUtils() {
        throw new UnsupportedOperationException("Can not be created！");
    }

    /**
     * 对象转Json字符串
     *
     * @param object
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> String toJson(T object) throws JsonSyntaxException {
        String gsonString = null;
        if (gson != null) {
            try {
                gsonString = gson.toJson(object);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return gsonString;
    }

    /**
     * Json字符串转对象
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T toObject(String json, Class<T> cls) throws JsonSyntaxException {
        T t = null;
        if (gson != null) {
            try {
                t = gson.fromJson(json, cls);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    /**
     * Json字符串转对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T toObject(String json, Type type) throws JsonSyntaxException {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(json, type);
        }
        return t;
    }

    /**
     * Json数组字符串转对象列表
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> List<T> toObjectList(String json, Class<T> cls) throws JsonSyntaxException {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }
}