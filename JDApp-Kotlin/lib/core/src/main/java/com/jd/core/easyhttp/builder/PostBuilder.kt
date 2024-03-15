package com.jd.core.easyhttp.builder

import android.os.Handler
import com.jd.core.easyhttp.utils.GsonUtil
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

/**
 * @author jd
 */
class PostBuilder(okHttpClient: OkHttpClient, delivery: Handler) :
    HttpBuilder(okHttpClient, delivery) {
    private var mediaType = "application/json;charset=utf-8".toMediaTypeOrNull()
    override fun createBuilder(): Request.Builder {
        val mBuilder = Request.Builder()
        url?.let { mBuilder.url(it) }
        var requestBody: RequestBody? = null
        requestBody = if (mediaType != null) {
            RequestBody.create(
                mediaType,
                GsonUtil.ser(params)
            )
        } else {
            val formBody = FormBody.Builder()
            addParams(formBody, params)
            formBody.build()
        }
        //这里的.post是区分get请求的关键步骤
        mBuilder.post(requestBody)
        return mBuilder
    }

    fun mediaType(mediaType: MediaType?): PostBuilder {
        this.mediaType = mediaType
        return this
    }

    //拼接头部参数
    fun appendHeaders(headers: Map<String?, String?>?): Headers? {
        val headerBuilder = Headers.Builder()
        if (headers.isNullOrEmpty()) {
            return null
        }
        for (key in headers.keys) {
            if (key != null) {
                headers[key]?.let { headerBuilder.add(key, it) }
            }
        }
        return headerBuilder.build()
    }

    //键值对拼接的参数
    private fun addParams(builder: FormBody.Builder?, params: Map<String, String?>?) {
        requireNotNull(builder) { "builder can not be null ." }
        if (!params.isNullOrEmpty()) {
            for (key in params.keys) {
                params[key]?.let { builder.add(key, it) }
            }
        }
    }

    fun json(): PostBuilder {
        mediaType = "application/json;charset=utf-8".toMediaTypeOrNull()
        return this
    }

    override fun url(url: String?): PostBuilder {
        super.url(url)
        return this
    }

    override fun tag(tag: String?): PostBuilder {
        super.tag(tag)
        return this
    }

    override fun headers(headers: Map<String, String?>?): PostBuilder {
        super.headers(headers)
        return this
    }

    override fun params(params: Map<String, String?>?): PostBuilder {
        super.params(params)
        return this
    }

    override fun retryCount(retryCount: Int): PostBuilder {
        super.retryCount(retryCount)
        return this
    }

    override fun once(once: Boolean): PostBuilder {
        super.once(once)
        return this
    }
}