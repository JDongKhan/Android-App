package com.jd.other.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

//import com.jd.app.IMyAidlInterface;

import androidx.annotation.Nullable;

public class JDService extends Service {

//    IMyAidlInterface.Stub mStub = new IMyAidlInterface.Stub() {
//       @Override
//        public int add(int arg1, int arg2) throws RemoteException {
//           return arg1 + arg2;
//       }
//    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
