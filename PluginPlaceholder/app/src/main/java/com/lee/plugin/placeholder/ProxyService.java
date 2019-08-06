package com.lee.plugin.placeholder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lee.plugin.standard.ServiceInterface;

/**
 * @author jv.lee
 * @date 2019-08-06
 * @description
 */
public class ProxyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        String className = intent.getStringExtra("className");

        //className = com.lee.plugin.app.TestService
        try {
            Class<?> testServiceClass = PluginManager.getInstance(this).getDexClassLoader().loadClass(className);
            Object testService = testServiceClass.newInstance();

            ServiceInterface serviceInterface = (ServiceInterface) testService;

            //注入组件环境
            serviceInterface.bindContext(this);

            serviceInterface.onStartCommand(intent, flags, startId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
