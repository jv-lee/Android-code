package com.lee.component.javapoet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lee.component.annotation.ARouter;

/**
 * @author jv.lee
 */
@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
