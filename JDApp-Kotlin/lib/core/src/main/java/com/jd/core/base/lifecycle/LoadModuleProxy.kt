package com.jd.core.base.lifecycle

import android.app.Application
import android.content.Context
import com.jd.core.base.IApplication
import com.jd.core.log.LogUtils
import java.util.ServiceLoader

/**
 * 加载组件代理类
 * 组件初始化的工作将由该代理类代理实现
 */
class LoadModuleProxy : IApplication {

    private var mLoader: ServiceLoader<IApplication> =
        ServiceLoader.load(IApplication::class.java)

    /**
     * 同[Application.attachBaseContext]
     * @param context Context
     */
    override fun onAttachBaseContext(context: Context) {
        mLoader.forEach {
            LogUtils.i("IApplication",it.toString())
            it.onAttachBaseContext(context)
        }
    }

    /**
     * 同[Application.onCreate]
     * @param application Application
     */
    override fun onCreate(application: Application) {
        mLoader.forEach { it.onCreate(application) }
    }

    /**
     * 同[Application.onTerminate]
     * @param application Application
     */
    override fun onTerminate(application: Application) {
        mLoader.forEach { it.onTerminate(application) }
    }

    /**
     * 不需要立即初始化的放在这里进行后台初始化
     */
    override fun onAsyncInit() {
        mLoader.forEach { it.onAsyncInit() }
    }
}