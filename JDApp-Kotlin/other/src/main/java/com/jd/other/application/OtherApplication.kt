package com.jd.other.application

import android.app.Application
import android.util.Log

import com.renny.mylibrary.IAppInit

/*
    @see https://github.com/ren93/initiator
 */

class OtherApplication : IAppInit {
    override fun init(application: Application) {
        Log.d("init==", "OtherApplication")
    }
}
