package com.jd.app;


import com.jd.app.plugin.DataAnalysis;
import com.jd.core.base.BaseApplication;
import com.renny.libcore.InitContext;

@InitContext
public class JDApplication extends BaseApplication {

    public void onCreate() {
        super.onCreate();
        DataAnalysis.collectionActivity(this);
    }

}
