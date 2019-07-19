package com.jd.home.application;

import android.app.Application;
import android.util.Log;

import com.renny.mylibrary.IAppInit;

/*
    @see https://github.com/ren93/initiator
 */

public class HomeApplication implements IAppInit {
    @Override
    public void init(Application application) {
        Log.d("init==", "HomeApplication");
    }
}
