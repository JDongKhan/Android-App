package com.jd.core.base

import android.app.Application
import android.content.Context

import com.alibaba.android.arouter.launcher.ARouter

import androidx.multidex.MultiDex
import com.jd.core.base.lifecycle.BaseActivityLifecycleCallbacks
import com.jd.core.base.lifecycle.LoadModuleProxy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import kotlin.system.exitProcess


open class BaseApplication : Application() {

    private val mCoroutineScope by lazy(mode = LazyThreadSafetyMode.NONE) { MainScope() }
    private val mLoadModuleProxy by lazy(mode = LazyThreadSafetyMode.NONE) { LoadModuleProxy() }

    private val isDebugARouter = true

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        appContext = this
        mLoadModuleProxy.onAttachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        //MultiDex分包方法 必须最先初始化
        MultiDex.install(this)
        // 全局监听 Activity 生命周期
        registerActivityLifecycleCallbacks(BaseActivityLifecycleCallbacks())

        if (isDebugARouter) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        //集成koin
        initKoin()
        //生命周期传递
        mLoadModuleProxy.onCreate(this)
        //异步初始化
        initAsync()
    }

    private fun initKoin(){
        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
        }
    }

    /**
     * 初始化第三方依赖
     */
    private fun initAsync() {
        // 开启一个 Default Coroutine 进行初始化不会立即使用的第三方
        mCoroutineScope.launch(Dispatchers.Default) {
            mLoadModuleProxy.onAsyncInit()
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        mLoadModuleProxy.onTerminate(this)
        mCoroutineScope.cancel()
    }

    /**
     * 退出应用
     */
    fun exitApp() {
        //用于杀掉当前进程
        android.os.Process.killProcess(android.os.Process.myPid())
        //参数0和1代表退出的状态，0表示正常退出，1表示异常退出
        exitProcess(0)
    }

    companion object {
        var appContext: BaseApplication? = null
            private set
    }

}
