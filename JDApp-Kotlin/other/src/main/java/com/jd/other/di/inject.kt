package com.jd.other.di

import com.jd.other.repository.OtherRepository
import com.jd.other.vm.OtherViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

class KoinTest {
    fun test() {
    }
}

// normalModule就是来管理常规的对象注入
val normalModule = module {
    factory { KoinTest() }
}

//单例
val singleModule = module {
    single { OtherRepository() }
}

//viewModel
val viewModelModule = module {
    viewModel {
        OtherViewModel(get())
    }
}

val moduleList = listOf(normalModule, singleModule,viewModelModule)