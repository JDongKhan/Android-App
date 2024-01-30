package com.jd.app


import com.jd.core.base.BaseApplication
import com.renny.libcore.InitContext

@InitContext
class JDApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
      //  DataAnalysis.collectionActivity(this)
    }

}
