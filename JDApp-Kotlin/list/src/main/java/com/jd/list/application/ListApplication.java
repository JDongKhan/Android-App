package com.jd.list.application;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.jd.core.base.IApplication;

/**
 * @author jd
 */
@AutoService(IApplication.class)
public class ListApplication implements IApplication {

    @Override
    public void onAttachBaseContext(@NonNull Context context) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        Log.d("init==", "ListApplication");
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    @Override
    public void onAsyncInit() {

    }
}
