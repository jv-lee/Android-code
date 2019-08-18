package com.lee.eventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lee.eventbus.model.ProductInfo;
import com.lee.eventbus.model.UserInfo;

import lee.eventbus.annotation.Subscribe;
import lee.eventbus.apt.EventBusIndex;
import lee.eventbus.core.EventBus;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        EventBus.getDefault().postSticky(new ProductInfo("相机",13888));
        startActivity(new Intent(this, SendActivity.class));
    }

    @Subscribe
    public void onEvent(UserInfo info) {
        Log.i(TAG, "onEvent: " + info.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}

