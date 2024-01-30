package com.jd.core.mvvm.m

import com.jd.core.network.model.Response

open class BaseRepository {

    suspend fun <T> apiCall(call: suspend () -> Response<T>): Response<T> {
        return call.invoke()
    }

    suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Response<T> {
        return try {
            call()
        } catch (e: Exception) {
            // An exception was thrown when calling the API so we're converting this to an IOException
            Response(-1,null,"请求异常")
        }
    }

}