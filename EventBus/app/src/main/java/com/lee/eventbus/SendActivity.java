package com.lee.eventbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lee.eventbus.model.ProductInfo;
import com.lee.eventbus.model.UserInfo;

import lee.eventbus.annotation.Subscribe;
import lee.eventbus.core.EventBus;

/**
 * @author jv.lee
 */
public class SendActivity extends AppCompatActivity {

    private static final String TAG = "SendActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new UserInfo("jv.lee", "20"));
    }

    @Subscribe(sticky = true)
    public void onEvent(ProductInfo info) {
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
