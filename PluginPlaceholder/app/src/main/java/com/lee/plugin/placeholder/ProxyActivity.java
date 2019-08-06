package com.lee.plugin.placeholder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.lee.plugin.standard.ActivityInterface;

import java.lang.reflect.Constructor;

/**
 * @author jv.lee
 * @date 2019-08-05
 * @description 代理Activity 也就是占位Activity
 */
public class ProxyActivity extends Activity {

    @Override
    public Resources getResources() {
        return PluginManager.getInstance(this).getResources();
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance(this).getDexClassLoader();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //真正的加载 插件里面的Activity
        String className = getIntent().getStringExtra("className");

        try {
            Class<?> pluginActivityClass = getClassLoader().loadClass(className);
            //实列化插件包里的activity
            Constructor<?> constructor = pluginActivityClass.getConstructor(new Class[]{});
            Object pluginActivity = constructor.newInstance(new Object[]{});
            ActivityInterface activityInterface = (ActivityInterface) pluginActivity;

            //注入
            activityInterface.bindContext(this);

            Bundle bundle = new Bundle();
            bundle.putString("appName", "我是宿主传入的参数");

            //执行插件的onCreate方法
            activityInterface.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        Log.d("ProxyActivity", "startActivity: " + className);
        //要给TestActivity 进栈
        Intent proxyIntent = new Intent(this, ProxyActivity.class);
        proxyIntent.putExtra("className", className);
        super.startActivity(proxyIntent);
    }

    @Override
    public ComponentName startService(Intent service) {
        String className = service.getStringExtra("className");
        Intent intent = new Intent(this, ProxyService.class);
        intent.putExtra("className", className);
        return super.startService(intent);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        //在宿主注册广播
        String testReceiverClassName = receiver.getClass().getName();
        return super.registerReceiver(new ProxyReceiver(testReceiverClassName), filter);
    }

    @Override
    public void sendBroadcast(Intent intent) {
        super.sendBroadcast(intent);
    }
}
