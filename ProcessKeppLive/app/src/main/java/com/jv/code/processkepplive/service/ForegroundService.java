package com.jv.code.processkepplive.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class ForegroundService extends Service {

    private static final int SERVICE_ID = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    //进程通信时需要创建 binder
    private class LocalBinder extends Binder{

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < 18) {
            //4.3
            startForeground(SERVICE_ID, new Notification());
        } else if (Build.VERSION.SDK_INT < 26) {
            //7.0
            startForeground(SERVICE_ID, new Notification());
            //删除通知栏消息
            startService(new Intent(this, InnerService.class));
        } else {
            //8.0
            //设置channel
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //设置低等级 越小通知信越低
            NotificationChannel channel = new NotificationChannel("channel", "xxx", NotificationManager.IMPORTANCE_MIN);
            if (null != manager) {
                manager.createNotificationChannel(channel);
                Notification notification = new NotificationCompat.Builder(this, "channel").build();

                startForeground(SERVICE_ID, notification);
                //            stopForeground(STOP_FOREGROUND_REMOVE);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public static class InnerService extends Service {

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            //启动同时关闭 取消通知栏提示
            startForeground(SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
    }

}
