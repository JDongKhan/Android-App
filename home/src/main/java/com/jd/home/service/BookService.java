package com.jd.home.service;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookService {

    @GET("/service/getIpInfo.php")
    public Call<ResponseBody> getShop(@Query("ip") String ip);

}
