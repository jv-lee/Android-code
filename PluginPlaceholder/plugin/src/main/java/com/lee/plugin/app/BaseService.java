package com.lee.plugin.app;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lee.plugin.standard.ServiceInterface;

/**
 * @author jv.lee
 * @date 2019-08-06
 * @description
 */
public class BaseService extends Service implements ServiceInterface {

    public Service service;

    @Override
    public void bindContext(Service service) {
        this.service = service;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public void onDestroy() {
    }
}
