package com.jd.core.easyhttp.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

/**
 * @author jd
 */
object GsonUtil {
    private val gsonObject: Gson
        private get() = GsonBuilder().serializeNulls().create()

    /**
     * 对象转Gson字符串
     *
     * @param object
     * @return
     */
    fun <T : Any?> ser(`object`: T): String {
        val gson = gsonObject
        return gson.toJson(`object`)
    }

    /**
     * Gson字符串转可序列化对象
     *
     * @param object
     * @param clazz
     * @return
     */
    fun <T : Any?> deser(`object`: String?, clazz: Class<T>?): T? {
        val gson = gsonObject
        var result: T? = null
        try {
            result = gson.fromJson(`object`, clazz)
        } catch (e: Exception) {
            result = null
            e.printStackTrace()
        }
        return result
    }

    /**
     * Gson字符串转可序列化对象
     *
     * @param object
     * @return
     */
    fun <T : Any?> deser(`object`: String?, type: Type?): T? {
        val gson = gsonObject
        var result: T? = null
        try {
            result = gson.fromJson(`object`, type)
        } catch (e: Exception) {
            result = null
            e.printStackTrace()
        }
        return result
    }

    fun <T : Any?> deserBequiet(`object`: String?, clazz: Class<T>?): T? {
        val gson = gsonObject
        val result: T?
        result = try {
            gson.fromJson(`object`, clazz)
        } catch (e: Exception) {
            null
        }
        return result
    }

    fun <T> Json2Result(class1: Class<T>, s: String?): T? {
        var result: T?
        try {
            result = Gson().fromJson(s, class1)
            Log.d("$class1------Json Msg", s!!)
        } catch (e: Exception) {
            result = null
            Log.e("$class1------Json Error", s!!)
        }
        return result
    }
}