package com.jd.core.network.model

data class Response<T>(val code:Int, val data:T?,val message:String?)
