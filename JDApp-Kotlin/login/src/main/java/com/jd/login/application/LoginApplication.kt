package com.jd.login.application

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.auto.service.AutoService
import com.jd.core.base.IApplication

@AutoService(IApplication::class)
class LoginApplication : IApplication {
    override fun onAttachBaseContext(context: Context) {
    }

    override fun onCreate(application: Application) {
        Log.d("init==", "LoginApplication")
    }

    override fun onTerminate(application: Application) {
    }

    override fun onAsyncInit() {
    }
}
