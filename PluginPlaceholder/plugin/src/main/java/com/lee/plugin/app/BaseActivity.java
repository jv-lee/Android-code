package com.lee.plugin.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.lee.plugin.standard.ActivityInterface;

import java.util.Objects;

/**
 * @author jv.lee
 * @date 2019-08-05
 * @description
 */
public class BaseActivity extends Activity implements ActivityInterface {

    public Activity activity;

    @Override
    public void bindContext(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }

    @Override
    public void setContentView(View view) {
        activity.setContentView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        activity.setContentView(layoutResID);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return activity.findViewById(id);
    }

    @Override
    public void startActivity(Intent intent) {
        //调用 bindContext 进来的 ProxyActivity的 startActivity方法
        Intent intentNew = new Intent();
        //设置包名+类名 全名称 TestActivity
        intentNew.putExtra("className", Objects.requireNonNull(intent.getComponent()).getClassName());
        activity.startActivity(intentNew);
    }

    @Override
    public ComponentName startService(Intent service) {
        Intent intentNew = new Intent();
        //TestService
        intentNew.putExtra("className", Objects.requireNonNull(service.getComponent()).getClassName());
        return activity.startService(intentNew);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return activity.registerReceiver(receiver, filter);
    }

    @Override
    public void sendBroadcast(Intent intent) {
        activity.sendBroadcast(intent);
    }
}
