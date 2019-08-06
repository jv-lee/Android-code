package com.lee.plugin.standard;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author jv.lee
 * @date 2019-08-06
 * @description
 */
public interface ServiceInterface {

    void bindContext(Service service);

    IBinder onBind(Intent intent);

    boolean onUnbind(Intent intent);

    void onCreate();

    int onStartCommand(Intent intent, int flags, int startId);

    void onDestroy();

}
