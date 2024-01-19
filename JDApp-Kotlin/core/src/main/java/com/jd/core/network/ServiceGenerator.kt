package com.jd.core.network

import android.util.Log
import com.jd.core.network.gson.BaseConverterFactory
import com.jd.core.utils.AppUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

class ServiceGenerator {
    private val retrofit: Retrofit
    private val client: OkHttpClient
    private val TAG = "ServiceGenerator"

    /**
     * 请求访问quest
     * response拦截器
     */
    private val interceptor = Interceptor { chain ->
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(request)
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        Log.e(TAG, "----------Request Start----------------")
        Log.e(TAG, "| " + request.toString() + request.headers().toString())
        Log.e(TAG, "| Response:" + AppUtils.unicodeToUTF_8(content))
        Log.e(TAG, "----------Request End:" + duration + "毫秒----------")
        response.newBuilder()
            .body(ResponseBody.create(mediaType, content))
            .build()
    }

    init {
        client = OkHttpClient.Builder() //添加log拦截器
            .addInterceptor(interceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL) //添加自定义的解析器
            .addConverterFactory(BaseConverterFactory.create()) //支持RxJava2
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    fun <S> createService(serviceClass: Class<S>?): S {
        return retrofit.create<S>(serviceClass)
    }

    companion object {
        const val API_BASE_URL = "http://ip.taobao.com/"
        var READ_TIMEOUT = 60
        var WRIT_TIMEOUT = 60
        var CONNECT_TIMEOUT = 60
        private var serviceGenerator: ServiceGenerator? = null
        @JvmStatic
        val instance: ServiceGenerator
            get() {
                if (serviceGenerator == null) {
                    synchronized(Any::class.java) {
                        if (serviceGenerator == null) {
                            serviceGenerator = ServiceGenerator()
                        }
                    }
                }
                return serviceGenerator!!
            }
    }
}