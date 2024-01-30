package com.jd.core.mvvm.vm

import androidx.lifecycle.ViewModel
import com.jd.core.mvvm.m.BaseRepository
import java.lang.reflect.ParameterizedType
import java.util.Objects


open class BaseViewModel<T:BaseRepository> : ViewModel() {

    protected var repository: T? = null

    init {
        val actualTypeArguments =
            (Objects.requireNonNull(this::class.java.genericSuperclass) as ParameterizedType).actualTypeArguments
        for (i in actualTypeArguments.indices) {
            val tClass: Class<Any>
            try {
                tClass = actualTypeArguments[i] as Class<Any>
            } catch (e: Exception) {
                continue
            }
            if (BaseRepository::class.java.isAssignableFrom(tClass)) {
                repository =  tClass.getConstructor().newInstance() as T
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}