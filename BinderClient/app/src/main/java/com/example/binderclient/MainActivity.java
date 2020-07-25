package com.example.binderclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.binderserver.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private IMyAidlInterface mMyAidlInterface;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            log("onServiceConnected enter. componentName = " + componentName);
            mMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
            testCases();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            log("onServiceDisconnected enter. componentName = " + componentName);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent("com.example.binderserver.myservice");
        intent.setPackage("com.example.binderserver");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    private void sendBytes(final int size, final int count) throws RemoteException {
        final byte[] bytes = new byte[size];
        for (int i = 0; i < count; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mMyAidlInterface.hello(bytes);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void testCases() {
        try {
            //testCase1();
            //testCase2();
            //testCase3();
            //testCase4();
            //testCase5();
            testCase6();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void testCase1() throws RemoteException {
        final int size = 1024 * 1024; // 1M
        sendBytes(size, 1);
    }

    private void testCase2() throws RemoteException {
        log("testCase2 enter.");
        final int size = 300 * 1024; // 300k
        sendBytes(size, 3); // 300k x 3
        log("testCase2 exit.");
    }

    private void testCase3() throws RemoteException {
        log("testCase3 enter.");
        testCase2();
        try {
            Thread.sleep(3 * 1000); // 3s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final int size = 116 * 1024; // 116k, crash
        byte[] bytes = new byte[size];
        log("send " + size + " bytes");
        mMyAidlInterface.hello(bytes);
    }

    private  void testCase4() throws RemoteException {
        log("testCase4 enter.");
        testCase2();
        try {
            Thread.sleep(3 * 1000); // 3s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final int size = 115 * 1024; // 115k, ok
        byte[] bytes = new byte[size];
        log("send " + size + " bytes");
        mMyAidlInterface.hello(bytes);
    }

    private  void testCase5() throws RemoteException {
        log("testCase5 enter.");
        testCase2();
        try {
            Thread.sleep(3 * 1000); // 3s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final int size = 115 * 1024 + 632; // 115k + 632, ok
        byte[] bytes = new byte[size];
        log("send " + size + " bytes");
        mMyAidlInterface.hello(bytes);
    }

    private  void testCase6() throws RemoteException {
        log("testCase5 enter.");
        testCase2();
        try {
            Thread.sleep(3 * 1000); // 3s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final int size = 115 * 1024 + 633; // 115k + 633, crash
        byte[] bytes = new byte[size];
        log("send " + size + " bytes");
        mMyAidlInterface.hello(bytes);
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }
}