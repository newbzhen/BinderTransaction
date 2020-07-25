package com.example.binderserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "MyService";
    public MyService() {
    }

    private IBinder mMyAidlInterface = new IMyAidlInterface.Stub() {
        @Override
        public void hello(byte[] data) throws RemoteException {
            log("hello enter.");
            try {
                final long time = 60 * 60 * 1000; // 1 day
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mMyAidlInterface;
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }
}
