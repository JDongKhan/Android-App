package com.jd.debug.application

import android.app.Application
import android.util.Log

import com.renny.libcore.AppInit
import com.renny.mylibrary.IAppInit

/*
    @see https://github.com/ren93/initiator
 */

@AppInit(priority = 22, delay = 1740, onlyInDebug = true)
class DebugApplication : IAppInit {
    override fun init(application: Application) {
        Log.d("init==", "DebugApplication")
    }
}
