package com.jd.other.network


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("/service/getIpInfo.php")
    fun getShop(@Query("ip") ip: String): Call<ResponseBody>

}
