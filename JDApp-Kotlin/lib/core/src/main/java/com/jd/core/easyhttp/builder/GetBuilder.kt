package com.jd.core.easyhttp.builder

import android.os.Handler
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * @author jd
 */
class GetBuilder(okHttpClient: OkHttpClient, delivery: Handler) :
    HttpBuilder(okHttpClient, delivery) {
    override fun createBuilder(): Request.Builder {
        val mBuilder = Request.Builder()
        if (params != null) {
            mBuilder.url(appendParams(url!!, params))
        } else {
            url?.let { mBuilder.url(it) }
        }
        return mBuilder
    }

    /**
     * get 参数拼在url后面
     * @param url
     * @param params
     * @return
     */
    private fun appendParams(url: String, params: Map<String, String?>?): String {
        var sb = StringBuilder()
        if (url.indexOf("?") == -1) {
            sb.append("$url?")
        } else {
            sb.append("$url&")
        }
        if (!params.isNullOrEmpty()) {
            for (key in params.keys) {
                sb.append(key).append("=").append(params[key]).append("&")
            }
        }
        sb = sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }
}