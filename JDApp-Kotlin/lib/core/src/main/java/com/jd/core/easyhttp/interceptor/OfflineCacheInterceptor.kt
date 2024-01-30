package com.jd.core.easyhttp.interceptor

import android.content.Context
import android.net.ConnectivityManager
import com.jd.core.utils.AppUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author jd
 */
class OfflineCacheInterceptor : Interceptor {
    /**
     * 离线的时候的缓存的过期时间
     */
    private var offlineCacheTime = 0
    fun setOfflineCacheTime(time: Int) {
        offlineCacheTime = time
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!isNetworkConnected(AppUtils.getApp())) {
            if (offlineCacheTime != 0) {
                val temp = offlineCacheTime
                request =
                    request.newBuilder() //                        .cacheControl(new CacheControl
                        //                                .Builder()
                        //                                .maxStale(60,TimeUnit.SECONDS)
                        //                                .onlyIfCached()
                        //                                .build()
                        //                        ) 两种方式结果是一样的，写法不同
                        .header("Cache-Control", "public, only-if-cached, max-stale=$temp")
                        .build()
                offlineCacheTime = 0
            } else {
                request =
                    request.newBuilder() //                        .cacheControl(new CacheControl
                        //                                .Builder()
                        //                                .maxStale(60,TimeUnit.SECONDS)
                        //                                .onlyIfCached()
                        //                                .build()
                        //                        ) 两种方式结果是一样的，写法不同
                        .header("Cache-Control", "no-cache")
                        .build()
            }
        }
        return chain.proceed(request)
    }

    companion object {
        private val offlineCacheInterceptor: OfflineCacheInterceptor? = null

        /**
         * 判断网络
         * @param context
         * @return
         */
        fun isNetworkConnected(context: Context?): Boolean {
            if (context != null) {
                val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val mNetworkInfo = mConnectivityManager.activeNetworkInfo
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable
                }
            }
            return false
        }
    }
}