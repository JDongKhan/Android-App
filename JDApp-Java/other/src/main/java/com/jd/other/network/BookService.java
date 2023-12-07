package com.jd.other.network;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookService {

    @GET("/service/getIpInfo.php")
    Observable<String> getShop(@Query("ip") String ip);

}
