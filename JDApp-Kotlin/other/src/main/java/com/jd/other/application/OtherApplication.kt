package com.jd.other.application

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.auto.service.AutoService
import com.jd.core.base.IApplication
import com.jd.core.utils.ProcessUtils
import com.jd.other.di.moduleList
import org.koin.core.context.loadKoinModules


@AutoService(IApplication::class)
class OtherApplication : IApplication {
    override fun onAttachBaseContext(context: Context) {

    }

    override fun onCreate(application: Application) {
        Log.d("init==", "OtherApplication")
        if (ProcessUtils.isMainProcess(application)) {
            loadKoinModules(moduleList)
        }
    }

    override fun onTerminate(application: Application) {

    }

    override fun onAsyncInit() {

    }
}
