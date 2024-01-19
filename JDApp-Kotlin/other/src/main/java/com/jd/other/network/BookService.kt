package com.jd.other.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("/service/getIpInfo.php")
    fun getShop(@Query("ip") ip: String?): Observable<String?>?
}