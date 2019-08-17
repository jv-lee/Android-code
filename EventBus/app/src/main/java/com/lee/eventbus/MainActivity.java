package com.lee.eventbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lee.eventbus.model.UserInfo;

import lee.eventbus.annotation.Subscribe;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Subscribe
    public void onEvent(UserInfo info) {
        Log.i(TAG, "onEvent: " + info.toString());
    }

    @Subscribe
    public void onEvent2(UserInfo info) {
        Log.i(TAG, "onEvent2: " + info.toString());
    }

}

