package com.lee.component;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lee.component.annotation.ARouter;

/**
 * @author jv.lee
 */
@ARouter(path = "/app/Main2Activity")
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
