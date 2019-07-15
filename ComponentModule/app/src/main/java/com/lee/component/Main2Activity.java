package com.lee.component;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lee.component.annotation.ARouter;
import com.lee.component.annotation.Parameter;

/**
 * @author jv.lee
 */
@ARouter(path = "/app/Main2Activity")
public class Main2Activity extends AppCompatActivity {

    @Parameter
    String username;
    @Parameter
    int gender;
    @Parameter
    boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
