package com.jv.code.processkepplive.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.ref.WeakReference;

public class KeepManager {
    private static final KeepManager mInstance = new KeepManager();

    public static KeepManager getInstance() {
        return mInstance;
    }

    private KeepManager() {

    }

    private KeepReceiver keepReceiver;

    //弱引用
    private WeakReference<Activity> mKeepAct;

    /**
     * 注册 开屏 关屏
     */
    public void registerKeep(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        keepReceiver = new KeepReceiver();
        context.registerReceiver(keepReceiver, filter);
    }

    /**
     * 注銷
     */
    public void unRegisterKeep(Context context) {
        if (null != keepReceiver) {
            context.unregisterReceiver(keepReceiver);
        }
    }

    /**
     * 开启一像素Activity
     */
    public void startKeep(Context context) {
        context.startActivity(new Intent(context, KeepActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 关闭一像素Activity
     */
    public void finishKeep(){
        if (null != mKeepAct) {
            Activity activity = mKeepAct.get();
            if (null != activity) {
                activity.finish();
            }
        }
    }


    public void setKeep(KeepActivity activity) {
        mKeepAct = new WeakReference<Activity>(activity);
    }

}
