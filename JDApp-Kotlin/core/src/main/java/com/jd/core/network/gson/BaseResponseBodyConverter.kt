package com.jd.core.network.gson

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.jd.core.exception.NetworkException
import okhttp3.ResponseBody
import okio.BufferedSource
import okio.Okio
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter
import java.io.IOException

/**
 * @author jd
 */
class BaseResponseBodyConverter<T> internal constructor(gson: Gson, adapter: TypeAdapter<out T>) :
    Converter<ResponseBody, T?> {
    private val gson: Gson
    private val adapter: TypeAdapter<out T>

    init {
        this.gson = gson
        this.adapter = adapter
    }

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T? {
        val buffer: BufferedSource = Okio.buffer(value.source())
        val jsonString: String = buffer.readUtf8()
        return try {
            val `object` = JSONObject(jsonString)
            val errorCode: Int = `object`.getInt("code")
            if (errorCode == 0) {
                val errorMsg: String = `object`.getString("errorMsg")
                throw NetworkException(errorMsg, errorCode)
            }
            val data: String = `object`.getString("data")
            if (data == null || "" == data) {
                null
            } else adapter.fromJson(data)
        } catch (e: JSONException) {
            e.printStackTrace()
            throw NetworkException(NetworkException.PARSE_ERROR_MSG, NetworkException.PARSE_ERROR)
        } finally {
            value.close()
        }
    }
}