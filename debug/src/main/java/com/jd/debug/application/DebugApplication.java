package com.jd.debug.application;

import android.app.Application;
import android.util.Log;

import com.renny.libcore.AppInit;
import com.renny.mylibrary.IAppInit;

/*
    @see https://github.com/ren93/initiator
 */

@AppInit(priority = 22, delay = 1740, onlyInDebug = true)
public class DebugApplication implements IAppInit {
    @Override
    public void init(Application application) {
        Log.d("init==", "DebugApplication");
    }
}
