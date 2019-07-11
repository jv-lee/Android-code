package com.lee.component.personal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lee.library.base.BaseActivity;
import com.lee.library.utils.Constants;

/**
 * @author jv.lee
 */
public class Personal_MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_main);
        Log.e(Constants.TAG, "common/Personal_MainActivity");
    }

    public void jumpApp(View view) {

    }

    public void jumpOrder(View view) {

    }

}
