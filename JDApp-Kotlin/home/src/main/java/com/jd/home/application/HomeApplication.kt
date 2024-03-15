package com.jd.home.application

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.auto.service.AutoService
import com.jd.core.base.IApplication

@AutoService(IApplication::class)
class HomeApplication : IApplication {
    override fun onAttachBaseContext(context: Context) {
    }

    override fun onCreate(application: Application) {
        Log.d("init==", "HomeApplication")
    }

    override fun onAsyncInit() {

    }


    override fun onTerminate(application: Application) {
    }


}
