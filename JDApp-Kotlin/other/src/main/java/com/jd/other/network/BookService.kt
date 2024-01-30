package com.jd.other.network

import com.jd.core.network.model.Response
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("/service/getIpInfo.php")
    fun getShop(@Query("ip") ip: String?): Observable<Response<String?>>?

    @GET("/service/getIpInfo.php")
    suspend fun getShop2(@Query("ip") ip: String?): Response<String?>
}