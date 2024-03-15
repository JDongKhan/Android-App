package com.jd.core.network.model

data class Response<T>(@JvmField val code:Int, @JvmField val data:T?,@JvmField val message:String?)
