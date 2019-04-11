package com.lee.code.pixel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Density.setDensity(getApplication(),this);
//        setContentView(R.layout.activity_density);
//        setContentView(R.layout.activity_main);
//        setContentView(R.layout.activity_percent_layout);
        setContentView(R.layout.activity_screen_adapter_layout);
    }
}
