package com.jd.core.base

import android.app.Application
import android.content.Context

import com.alibaba.android.arouter.launcher.ARouter

import androidx.multidex.MultiDex


open class BaseApplication : Application() {

    private val isDebugARouter = true

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        appContext = this
    }

    override fun onCreate() {
        super.onCreate()
        //MultiDex分包方法 必须最先初始化
        MultiDex.install(this)


        if (isDebugARouter) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }


    /**
     * 退出应用
     */
    fun exitApp() {
        //用于杀掉当前进程
        android.os.Process.killProcess(android.os.Process.myPid())
        //参数0和1代表退出的状态，0表示正常退出，1表示异常退出
        System.exit(0)
    }

    companion object {
        var appContext: BaseApplication? = null
            private set
    }

}
