package com.jd.core.easyhttp.okcallback

import com.google.gson.internal.`$Gson$Types`
import com.jd.core.utils.ToastUtils
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author jd
 */
abstract class NetworkCallback<T> {
    /**
     * 请求网络之前，一般展示loading
     */
    fun onBefore() {}

    /**
     * 请求网络结束，消失loading
     */
    fun onAfter() {}

    /**
     * 监听上传图片的进度(目前支持图片上传,其他重写这个方法无效)
     * @param total
     * @param progress
     */
    fun inProgress(total: Long, progress: Float) {}

    /**
     * 错误信息
     * @param errorMessage
     */
    fun onError(errorMessage: String?) {
        ToastUtils.showLong(errorMessage)
    }

    abstract fun onSuccess(response: T?)


    private fun getSuperclassTypeParameter(subclass: Class<*>): Type? {
        val superclass = subclass.genericSuperclass
        if (superclass is Class<*>) {
            return null
        }
        val parameterized = superclass as ParameterizedType
        return `$Gson$Types`.canonicalize(parameterized.actualTypeArguments[0])
    }

    val type: Type?
        get() = getSuperclassTypeParameter(javaClass)
}