package com.jd.other.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException//import com.jd.app.IMyAidlInterface;

class JDService : Service() {

    //    IMyAidlInterface.Stub mStub = new IMyAidlInterface.Stub() {
    //       @Override
    //        public int add(int arg1, int arg2) throws RemoteException {
    //           return arg1 + arg2;
    //       }
    //    };

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onUnbind(intent: Intent): Boolean {
        return super.onUnbind(intent)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
