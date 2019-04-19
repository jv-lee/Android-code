package com.lee.code.livedatabus;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lee.code.livedatabus.google.LiveDataBus;


/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //事件订阅 观察者
//        LiveDataBus.getInstance().getChannel("event",String.class).observe(this, s -> {
//            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
//        });
        LiveDataBus.getInstance().getChannel("event",String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
//        LiveDataBus.getInstance().injectBus(this);

    }

//    @InjectBus(value = "event")
    public void event(String object){
        Toast.makeText(this, object, Toast.LENGTH_SHORT).show();
    }

    public void jump(View view) {
//        LiveDataBus.getInstance()
//                .getChannel("event")
//                .setValue("触发事件");
//        new ChildThread().start();
        startActivity(new Intent(this,SceondActivity.class));
    }

    public void start(View view) {
        startActivity(new Intent(this,SceondActivity.class));
//        startActivity(new Intent(this,ViewModelActivity.class));
    }
}
