package com.lee.eventbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.lee.eventbus.model.UserInfo;

import lee.eventbus.annotation.Subscribe;

public class SendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
    }

    @Subscribe
    public void onEvent(UserInfo info) {
        Toast.makeText(this, info.toString(), Toast.LENGTH_SHORT).show();
    }

}
