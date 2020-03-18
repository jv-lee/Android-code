package com.lee.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lee.library.utils.Constants;

/**
 * @author jv.lee
 * @date 2019/7/10.
 * @description
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(Constants.TAG, "common/BaseActivity");
    }
}
