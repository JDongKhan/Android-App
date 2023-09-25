package com.jd.core.network

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    val API_BASE_URL = "http://ip.taobao.com/"
    var READ_TIMEOUT = 60
    var WRIT_TIMEOUT = 60
    var CONNECT_TIMEOUT = 60
    private val httpClient = OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(WRIT_TIMEOUT.toLong(), TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)

    private val builder = Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create())

    fun <S> createService(serviceClass: Class<S>): S {
        return createService(serviceClass, null)
    }

    private fun <S> createService(serviceClass: Class<S>, authToken: String?): S {
        if (authToken != null) {
            httpClient.addInterceptor { chain ->
                val original = chain.request()

                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                        .method(original.method(), original.body())

                val request = requestBuilder.build()
                chain.proceed(request)
            }
        }

        val client = httpClient
                // 日志拦截器
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        val retrofit = builder.client(client).build()
        return retrofit.create(serviceClass)
    }
}
