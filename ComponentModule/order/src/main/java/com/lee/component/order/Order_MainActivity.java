package com.lee.component.order;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lee.library.base.BaseActivity;
import com.lee.library.utils.Constants;

/**
 * @author jv.lee
 */
public class Order_MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_main);
        Log.e(Constants.TAG, "common/Order_MainActivity");
    }


    public void jumpApp(View view) {

    }

    public void jumpPersonal(View view) {

    }

}
