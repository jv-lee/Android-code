package com.lee.component;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lee.component.annotation.ARouter;
import com.lee.component.annotation.Parameter;
import com.lee.component.api.RouterManager;

/**
 * @author jv.lee
 */
@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {

    @Parameter
    String name;
    @Parameter(name = "agex")
    int age = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpPersonal(View view) {
        RouterManager.getInstance()
                .build("/personal/Personal_MainActivity")
                .navigation(this);
    }

    public void jumpOrder(View view) {
        RouterManager.getInstance()
                .build("/order/Order_MainActivity")
                .navigation(this);
    }
}
