package com.lee.hookproxy;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author jv.lee
 * @date 2019-08-09
 * @description
 */
public class TestActivity extends Activity
{
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
