package com.lee.code.livedatabus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lee.code.livedatabus.livedata.LiveDataBus;

/**
 * @author jv.lee
 */
public class SceondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sceond);
//        LiveDataBus.getInstance()
//                .getChannel("event",String.class)
//                .observe(this, new Observer<String>() {
//                    @Override
//                    public void onChanged(@Nullable String s) {
//                        Toast.makeText(SceondActivity.this, s, Toast.LENGTH_SHORT).show();
//                    }
//                });

    }

    public void send(View view) {
        LiveDataBus.getInstance().getChannel("event").setValue("图片获取到了");
    }
}
