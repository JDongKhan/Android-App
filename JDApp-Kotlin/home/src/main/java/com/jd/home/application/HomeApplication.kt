package com.jd.home.application

import android.app.Application
import android.util.Log

import com.renny.mylibrary.IAppInit

/*
    @see https://github.com/ren93/initiator
 */

class HomeApplication : IAppInit {
    override fun init(application: Application) {
        Log.d("init==", "HomeApplication")
    }
}
