package com.jd.setting.application

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.auto.service.AutoService
import com.jd.core.base.IApplication


@AutoService(IApplication::class)
class SettingApplication : IApplication {
    override fun onAttachBaseContext(context: Context) {

    }

    override fun onCreate(application: Application) {
        Log.d("init==", "SettingApplication")
    }

    override fun onTerminate(application: Application) {

    }

    override fun onAsyncInit() {

    }

}
