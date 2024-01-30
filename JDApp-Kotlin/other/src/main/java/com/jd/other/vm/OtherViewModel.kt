package com.jd.other.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.jd.core.log.LogUtils
import com.jd.core.mvvm.vm.BaseViewModel
import com.jd.other.repository.OtherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OtherViewModel(private val repo: OtherRepository) : BaseViewModel<OtherRepository>() {

    val text by lazy { MutableLiveData<String>("") }

    val user: LiveData<String> = liveData {
        val data = loadUser();
        emit(data)
    }

    private suspend fun loadUser(): String {
        delay(2000)
        return "name"
    }

    fun request() {
      val r =  viewModelScope.launch {
            val responseAsync = async ( SupervisorJob() + Dispatchers.IO) {
                return@async repo.getShop();
            }
            val result = responseAsync.await()
            LogUtils.d("network",result?.data)
            text.value = result.data
        }

        LogUtils.d("network",r.toString())
    }
}