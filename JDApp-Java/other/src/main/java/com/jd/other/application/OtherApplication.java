package com.jd.other.application;

import android.app.Application;
import android.util.Log;

import com.renny.mylibrary.IAppInit;

/*
    @see https://github.com/ren93/initiator
 */

public class OtherApplication implements IAppInit {
    @Override
    public void init(Application application) {
        Log.d("init==", "OtherApplication");
    }
}
