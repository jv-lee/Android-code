package com.lee.code.okhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.lee.code.okhttp.network.IJsonDataListener;

public class MainActivity extends AppCompatActivity {

    String url = "http://apis.juhe.cn/idcard/index?cardno=431281199506051018&dtype=json&key=8563abcffdc8eb7a2aacf08268c1f11b";
//    String url = "xxx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttp.sendJsonRequest(null, url, ResponseBean.class, new IJsonDataListener<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean data) {
                Toast.makeText(MainActivity.this, data.getResult().getBirthday(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
