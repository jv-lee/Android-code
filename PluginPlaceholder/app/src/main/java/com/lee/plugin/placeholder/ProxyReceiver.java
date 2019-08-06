package com.lee.plugin.placeholder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lee.plugin.standard.ReceiverInterface;

/**
 * @author jv.lee
 * @date 2019-08-06
 * @description
 */
public class ProxyReceiver extends BroadcastReceiver {

    private String testReceiverClassName;

    public ProxyReceiver(String testReceiverClassName) {
        this.testReceiverClassName = testReceiverClassName;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Class<?> testReceiverClass = PluginManager.getInstance(context).getDexClassLoader().loadClass(testReceiverClassName);

            Object testReceiver = testReceiverClass.newInstance();

            ReceiverInterface receiverInterface = (ReceiverInterface) testReceiver;

            receiverInterface.onReceive(context, intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
