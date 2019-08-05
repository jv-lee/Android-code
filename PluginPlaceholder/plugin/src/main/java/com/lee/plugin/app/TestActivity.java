package com.lee.plugin.app;

import android.os.Bundle;
import android.widget.Toast;

/**
 * @author jv.lee
 * @date 2019-08-05
 * @description
 */
public class TestActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toast.makeText(activity, "TestActivity", Toast.LENGTH_SHORT).show();
    }
}
