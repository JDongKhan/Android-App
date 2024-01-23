package com.jd.core.easyhttp

import android.os.Environment
import android.os.Handler
import android.os.Looper
import com.jd.core.easyhttp.builder.DownloadBuilder
import com.jd.core.easyhttp.builder.GetBuilder
import com.jd.core.easyhttp.builder.PostBuilder
import com.jd.core.easyhttp.builder.UploadBuilder
import com.jd.core.easyhttp.interceptor.LoggerInterceptor
import com.jd.core.easyhttp.interceptor.NetCacheInterceptor
import com.jd.core.easyhttp.interceptor.OfflineCacheInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLSession

/**
 * @author jd
 */
class EasyHttp private constructor() {


    val delivery: Handler = Handler(Looper.getMainLooper())

    val okHttpClient: OkHttpClient = OkHttpClient.Builder() //设置缓存文件路径，和文件大小
        .cache(
            Cache(
                File(
                    Environment.getExternalStorageDirectory().toString() + "/okhttp_cache/"
                ), (50 * 1024 * 1024).toLong()
            )
        )
        .hostnameVerifier { hostname: String?, session: SSLSession? -> true }
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(
            10,
            TimeUnit.SECONDS
        ) //这里是网上对cookie的封装 github : https://github.com/franmontiel/PersistentCookieJar
        //如果你的项目没有遇到cookie管理或者你想通过网络拦截自己存储，那么可以删除persistentcookiejar包
        //                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApplication.getContext())))
        .addInterceptor(LoggerInterceptor())
        .addInterceptor(OfflineCacheInterceptor())
        .addNetworkInterceptor(NetCacheInterceptor())
        .build()



    init {
        //证书信任
    }

    /**
     * tag取消网络请求
     */
    fun cancelOkhttpTag(tag: String) {
        val dispatcher = okHttpClient.dispatcher()
        synchronized(dispatcher) {

            //请求列表里的，取消网络请求
            for (call in dispatcher.queuedCalls()) {
                if (tag == call.request().tag()) {
                    call.cancel()
                }
            }
            //正在请求网络的，取消网络请求
            for (call in dispatcher.runningCalls()) {
                if (tag == call.request().tag()) {
                    call.cancel()
                }
            }
        }
    }

    companion object {
        private var easyOk: EasyHttp? = null
        @JvmStatic
        val instance: EasyHttp
            get() {
                if (easyOk == null) {
                    synchronized(EasyHttp::class.java) {
                        if (easyOk == null) {
                            easyOk = EasyHttp()
                        }
                    }
                }
                return easyOk!!
            }

        fun get(): GetBuilder {
            return GetBuilder(instance!!.okHttpClient, instance!!.delivery)
        }

        fun post(): PostBuilder {
            return PostBuilder(instance!!.okHttpClient, instance!!.delivery)
        }

        fun upload(): UploadBuilder {
            return UploadBuilder(instance!!.okHttpClient, instance!!.delivery)
        }

        fun download(): DownloadBuilder {
            return DownloadBuilder(instance!!.okHttpClient, instance!!.delivery)
        }
    }
}