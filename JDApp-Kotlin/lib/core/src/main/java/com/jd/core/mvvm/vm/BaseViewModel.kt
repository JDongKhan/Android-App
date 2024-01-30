package com.jd.core.mvvm.vm

import android.os.Bundle
import androidx.lifecycle.ViewModel


open class BaseViewModel : ViewModel() {
    open fun init(arguments: Bundle?) {
    }

    override fun onCleared() {
        super.onCleared()
    }
}