package com.jd.setting.application

import android.app.Application
import android.util.Log

import com.renny.mylibrary.IAppInit

/*
    @see https://github.com/ren93/initiator
 */

class SettingApplication : IAppInit {
    override fun init(application: Application) {
        Log.d("init==", "HomeApplication")
    }
}
