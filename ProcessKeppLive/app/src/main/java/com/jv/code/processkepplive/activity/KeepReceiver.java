package com.jv.code.processkepplive.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class KeepReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //关屏
        if (TextUtils.equals(action, Intent.ACTION_SCREEN_OFF)) {
            KeepManager.getInstance().startKeep(context);
        }
        //开屏
        else if (TextUtils.equals(action, Intent.ACTION_SCREEN_ON)) {
            KeepManager.getInstance().finishKeep();
        }

    }
}
