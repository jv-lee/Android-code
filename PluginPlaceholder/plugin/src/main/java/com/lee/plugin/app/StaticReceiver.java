package com.lee.plugin.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author jv.lee
 * @date 2019-08-07
 * @description 插件静态广播
 */
public class StaticReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "我是静态注册的广播 plugin.static_receiver", Toast.LENGTH_SHORT).show();
    }
}
