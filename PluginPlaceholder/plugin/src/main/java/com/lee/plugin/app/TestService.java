package com.lee.plugin.app;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author jv.lee
 * @date 2019-08-06
 * @description
 */
public class TestService extends BaseService {

    private static final String TAG = "TestService";

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启子线程执行耗时任务
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Log.d(TAG, "run: 插件Service正在运行中...");
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
