package com.jd.core.network;

import android.util.Log;

import com.jd.core.network.gson.BaseConverterFactory;
import com.jd.core.utils.AppUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ServiceGenerator {
    public static final String API_BASE_URL = "http://ip.taobao.com/";
    public static int READ_TIMEOUT = 60;
    public static int WRIT_TIMEOUT = 60;
    public static int CONNECT_TIMEOUT = 60;

    private Retrofit retrofit;
    private OkHttpClient client;

    private String TAG = "ServiceGenerator";

    /**
     * 请求访问quest
     * response拦截器
     */
    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(request);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Log.e(TAG, "----------Request Start----------------");
            Log.e(TAG, "| " + request.toString() + request.headers().toString());
            Log.e(TAG, "| Response:" + AppUtils.unicodeToUTF_8(content));
            Log.e(TAG, "----------Request End:" + duration + "毫秒----------");
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
    };

    public ServiceGenerator() {
        client = new OkHttpClient.Builder()
                //添加log拦截器
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                //添加自定义的解析器
                .addConverterFactory(BaseConverterFactory.create())
                //支持RxJava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    private static ServiceGenerator serviceGenerator;
    public static ServiceGenerator getInstance() {
        if (serviceGenerator == null) {
            synchronized (Object.class) {
                if (serviceGenerator == null) {
                    serviceGenerator = new ServiceGenerator();
                }
            }
        }
        return serviceGenerator;
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
