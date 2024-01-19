package com.jd.core.easyhttp.builder

import android.os.Handler
import android.text.TextUtils
import com.jd.core.easyhttp.okcallback.NetworkCallback
import com.jd.core.easyhttp.utils.GsonUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.Date

/**
 * @author jd
 */
abstract class HttpBuilder(
    protected var okHttpClient: OkHttpClient,
    protected var mDelivery: Handler
) {
    /**
     * url
     */
    protected var url: String? = null

    /**
     * tag
     */
    protected var tag: String? = null

    /**
     * 参数
     */
    protected var params: Map<String, String?>? = null

    /**
     * header
     */
    protected var headers: Map<String, String?>? = null

    /**
     * 默认同时多次请求一个接口 只请求一次
     */
    protected var once = false

    /**
     * 默认不重连
     */
    protected var retryCount = 0
    protected var currentRetryCount = 0
    private var okHttpRequest: Request? = null
    protected var callback: NetworkCallback<*>? = null
    fun build(): HttpBuilder {
        val mBuilder = createBuilder()
        if (!TextUtils.isEmpty(tag)) {
            mBuilder.tag(tag)
        }
        val headers1 = appendHeaders(headers)
        if (headers1 != null) {
            mBuilder.headers(headers1)
        }
        mBuilder.addHeader("Request-Time", System.currentTimeMillis().toString())
        mBuilder.addHeader("Begin-Http-Time", Date().toString())
        okHttpRequest = mBuilder.build()
        return this
    }

    protected abstract fun createBuilder(): Request.Builder

    /**
     * 获取tag
     * @return
     */
    private fun getTag(): String? {
        return if (!TextUtils.isEmpty(tag)) {
            tag
        } else url
    }

    fun removeOnceTag() {
        if (once) {
            val tag = getTag()
            onceTagList.remove(tag)
        }
    }

    /**
     * 前置判断
     * @return
     */
    protected fun canRequest(): Boolean {
        if (once) {
            val tag = getTag()
            if (onceTagList.contains(tag)) {
                return false
            }
            onceTagList.add(tag)
        }
        return true
    }

    /**
     * 失败请求
     * @param call
     * @param e
     * @param resultCall
     * @param callback
     */
    protected open fun doFailureCallback(
        call: Call,
        e: IOException?,
        resultCall: NetworkCallback<*>?,
        callback: Callback?
    ) {
        if (e is SocketException) {
        } else {
            //如果在重连的情况下，是主动取消网络是java.net.SocketException: Socket closed
            if (currentRetryCount < retryCount && retryCount > 0) {
                // 如果超时并未超过指定次数，则重新连接
                currentRetryCount++
                okHttpClient.newCall(call.request()).enqueue(callback)
                return
            }
        }
        removeOnceTag()
        if (resultCall == null) {
            return
        }
        mDelivery.post {
            resultCall.onAfter()
            var errorMsg = "服务器异常,请稍后再试"
            if (e is ConnectException) {
                errorMsg = "网络不可用,请检查网络"
            } else if (e is SocketTimeoutException) {
                errorMsg = "请求超时,请稍后再试"
            }
            resultCall.onError(errorMsg)
        }
    }

    @Throws(IOException::class)
    protected open fun doSuccessCallback(
        call: Call,
        response: Response,
        resultCall: NetworkCallback<*>?,
        callback: Callback?
    ) {
        removeOnceTag()
        if (resultCall == null) {
            return
        }
        //网络请求成功
        if (response.isSuccessful) {
            val result = response.body()!!.string()
            var successObject: Any? = null
            successObject = if (resultCall.type == null) {
                result
            } else {
                GsonUtil.deser<Any>(result, resultCall.type)
            }
            if (successObject == null) {
                successObject = result
            }
            val finalSuccessObject = successObject
            mDelivery.post {
                resultCall.onAfter()
                resultCall.onSuccess(finalSuccessObject as Nothing?)
            }
        } else {
            //接口请求确实成功了，code 不是 200..299
            val errorMsg = response.body()!!.string()
            mDelivery.post {
                resultCall.onAfter()
                resultCall.onError(errorMsg)
            }
        }
    }

    /**
     * 非封装单独使用
     * @param resultCall
     */
    fun enqueue(resultCall: NetworkCallback<*>?) {
        if (okHttpRequest == null) {
            return
        }
        callback = resultCall
        if (!canRequest()) {
            return
        }
        if (resultCall != null) {
            mDelivery.post { resultCall.onBefore() }
        }
        okHttpClient.newCall(okHttpRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                doFailureCallback(call, e, resultCall, this)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                doSuccessCallback(call, response, resultCall, this)
            }
        })
    }

    open fun url(url: String?): HttpBuilder {
        this.url = url
        return this
    }

    open fun retryCount(retryCount: Int): HttpBuilder {
        this.retryCount = retryCount
        return this
    }

    open fun once(once: Boolean): HttpBuilder {
        this.once = once
        return this
    }

    open fun tag(tag: String?): HttpBuilder {
        this.tag = tag
        return this
    }

    open fun headers(headers: Map<String, String?>?): HttpBuilder {
        this.headers = headers
        return this
    }

    open fun params(params: Map<String, String?>?): HttpBuilder {
        this.params = params
        return this
    }

    private fun appendHeaders(headers: Map<String, String?>?): Headers? {
        val headerBuilder = Headers.Builder()
        if (headers.isNullOrEmpty()) {
            return null
        }
        for (key in headers.keys) {
            headerBuilder.add(key, headers[key])
        }
        return headerBuilder.build()
    }

    companion object {
        private val onceTagList = ArrayList<String?>()
    }
}