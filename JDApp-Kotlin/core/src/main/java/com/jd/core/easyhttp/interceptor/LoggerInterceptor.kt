package com.jd.core.easyhttp.interceptor

import android.util.Log
import com.jd.core.easyhttp.EasyHttp
import com.jd.core.utils.AppUtils
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException

/**
 * @author jd
 */
class LoggerInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val httpStart = System.currentTimeMillis()
        val log =
            "1".equals(originalRequest.header("Logger"), ignoreCase = true)
        if (log) {
            Log.d("network", "请求地址:" + originalRequest.url())
        } else {
            val stringBuilder = StringBuilder()
            stringBuilder.append("客户端开始请求:")
            stringBuilder.append(originalRequest.url())
            stringBuilder.append("\n开始请求时间戳:")
            stringBuilder.append(AppUtils.getDeviceId())
            stringBuilder.append("#")
            Log.i("network", stringBuilder.toString())
        }
        val request = originalRequest.newBuilder().removeHeader("log-able").build()
        var response: Response? = try {
            chain.proceed(request)
        } catch (e: IOException) {
            printErrorLog(request, e)
            throw e
        }
        val now = System.currentTimeMillis()
        printSuccessLog(request, response, now - httpStart)
        return response!!
    }

    @Throws(IOException::class)
    private fun printErrorLog(request: Request, exception: IOException) {
        val stringBuilder = StringBuilder()
        val requestString = bodyToString(request)
        stringBuilder.append("客户端开始请求(")
        stringBuilder.append(AppUtils.getDeviceId())
        val url = request.url().toString()
        stringBuilder.append("\n请求地址:")
        stringBuilder.append(url)
        stringBuilder.append("\n请求内容:")
        stringBuilder.append(requestString)
        //响应报文
        stringBuilder.append("\n响应内容:")
        // 生成response报文
        stringBuilder.append(exception.message)
        Log.i("network", stringBuilder.toString())
    }

    @Throws(IOException::class)
    private fun printSuccessLog(request: Request, response: Response?, time: Long) {
        val stringBuilder = StringBuilder()
        val requestString = bodyToString(request)
        stringBuilder.append("客户端开始请求(")
        stringBuilder.append(AppUtils.getDeviceId())
        stringBuilder.append(")")
        stringBuilder.append("请求耗时：" + time + "ms")
        val url = request.url().toString()
        stringBuilder.append("\n请求地址:")
        stringBuilder.append(url)
        stringBuilder.append("\n请求内容:")
        stringBuilder.append(requestString)
        //响应报文
        stringBuilder.append("\nHTTP状态码:")
        stringBuilder.append(response?.code())
        if (response?.headers() != null) {
            stringBuilder.append(
                """
    
    响应头:
    ${response.headers()}
    """.trimIndent()
            )
        }
        stringBuilder.append("\n响应Cookie:")
        stringBuilder.append(
            EasyHttp.instance.okHttpClient.cookieJar().loadForRequest(HttpUrl.get(url))
                .toString()
        )
        stringBuilder.append("\n响应内容:")
        // 生成response报文
        stringBuilder.append(response?.peekBody(1024)?.string())
        Log.i("network", stringBuilder.toString())
    }

    /**
     * body转字符串
     *
     * @param request
     * @return
     */
    private fun bodyToString(request: Request): String {
        return if ("POST" != request.method()) {
            ""
        } else try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            "something error when show requestBody."
        }
    }
}