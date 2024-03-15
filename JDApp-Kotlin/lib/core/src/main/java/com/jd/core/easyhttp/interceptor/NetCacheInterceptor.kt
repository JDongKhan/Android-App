package com.jd.core.easyhttp.interceptor

import android.text.TextUtils
import com.jd.core.utils.SPUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author jd
 */
class NetCacheInterceptor : Interceptor {
    /**
     * 30在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0
     */
    private var onlineCacheTime = 0
    fun setOnlineTime(time: Int) {
        onlineCacheTime = time
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder1 = request.newBuilder()

        //这里坐了自动解析头部和取值。之前一个项目要用头部的Token字段。我也不知道为什么不用cookie
        //(到时候最好用csdn登录来做)
        val token = SPUtils.getInstance().getString("USER_TOKEN", "")
        if (!TextUtils.isEmpty(token)) {
            builder1.addHeader("Token", token)
                .build()
        }
        request = builder1.build()
        val response = chain.proceed(request)
        val list = response.headers.values("Token")
        if (list.isNotEmpty()) {
            SPUtils.getInstance().put("USER_TOKEN", list[0])
        }
        return if (onlineCacheTime != 0) {
            //如果有时间就设置缓存
            val temp = onlineCacheTime
            val response1 = response.newBuilder()
                .header("Cache-Control", "public, max-age=$temp")
                .removeHeader("Pragma")
                .build()
            onlineCacheTime = 0
            response1
        } else {
            //如果没有时间就不缓存
            response.newBuilder()
                .header("Cache-Control", "no-cache")
                .removeHeader("Pragma")
                .build()
        }
        //        return response;
    }
}