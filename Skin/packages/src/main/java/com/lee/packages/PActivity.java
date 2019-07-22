package com.lee.packages;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author jv.lee
 * @date 2019-07-21
 * @description
 */
public class PActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
