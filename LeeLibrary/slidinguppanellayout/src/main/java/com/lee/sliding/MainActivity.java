package com.lee.sliding;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SlidingUpPanelLayout slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setAnchorPoint(1.0F);
    }
}
