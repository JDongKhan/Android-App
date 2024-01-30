package com.jd.list.application;

import android.app.Application;
import android.util.Log;

import com.renny.mylibrary.IAppInit;

/**
 * @author jd
 * @see https://github.com/ren93/initiator
 */
public class ListApplication implements IAppInit {
    @Override
    public void init(Application application) {
        Log.d("init==", "ListApplication");
    }
}
