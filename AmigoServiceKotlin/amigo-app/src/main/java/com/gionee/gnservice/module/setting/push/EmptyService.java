package com.gionee.gnservice.module.setting.push;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * Author: wangxin <br/>
 * Date: 12-7-4 <br/>
 * This is a empty service used for avoid application stopped so that our thread can go on running.
 */
public class EmptyService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void keepApplication(Context context) {
        context.startService(new Intent(context, EmptyService.class));
    }

    public static void cancelKeep(Context context) {
        context.stopService(new Intent(context, EmptyService.class));
    }
}
