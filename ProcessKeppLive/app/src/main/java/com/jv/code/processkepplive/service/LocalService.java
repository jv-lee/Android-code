package com.jv.code.processkepplive.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.jv.code.processkepplive.KeepTest;

public class LocalService extends ForegroundService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KeepTest.keepTest();
        bindService(new Intent(LocalService.this, RemoteService.class), mConnection, Service.BIND_IMPORTANT);
        return super.onStartCommand(intent, flags, startId);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //
            startService(new Intent(LocalService.this, RemoteService.class));
            bindService(new Intent(LocalService.this, RemoteService.class), mConnection, Service.BIND_IMPORTANT);
        }
    };

}
