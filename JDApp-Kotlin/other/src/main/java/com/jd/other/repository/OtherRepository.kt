package com.jd.other.repository

import com.jd.core.mvvm.m.BaseRepository
import com.jd.core.network.Network
import com.jd.core.network.model.Response
import com.jd.other.network.BookService

class OtherRepository : BaseRepository() {

    private val api by lazy {
        Network.instance.createService(BookService::class.java)
    }

    suspend fun getShop() : Response<String?> {
        return safeApiCall {
            api.getShop2("63.223.108.42")
        }
    }
}