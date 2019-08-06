package com.lee.plugin.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.lee.plugin.standard.ReceiverInterface;

/**
 * @author jv.lee
 * @date 2019-08-06
 * @description
 */
public class TestReceiver extends BroadcastReceiver implements ReceiverInterface {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "我是插件中的广播", Toast.LENGTH_SHORT).show();
    }
}
