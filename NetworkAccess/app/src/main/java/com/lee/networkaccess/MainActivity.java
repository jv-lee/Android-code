package com.lee.networkaccess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import jv.lee.library.NetworkManager;
import jv.lee.library.listener.NetChangeObserver;
import jv.lee.library.type.NetType;
import jv.lee.library.utils.Constants;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity implements NetChangeObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkManager.getDefault().registerObserver(this);
    }

    @Override
    public void onConnect(NetType type) {
        Log.i(Constants.LOG_TAG, "onConnect: " + type.name());
    }

    @Override
    public void onDisConnect() {
        Log.i(Constants.LOG_TAG, "onDisConnect: close");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消监听
        NetworkManager.getDefault().unRegisterObserver(this);
        //Main界面需要取消广播注册
        NetworkManager.getDefault().unRegister();
    }
}
