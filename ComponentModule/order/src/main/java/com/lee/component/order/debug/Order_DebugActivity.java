package com.lee.component.order.debug;

import android.os.Bundle;
import android.util.Log;

import com.lee.component.order.R;
import com.lee.library.base.BaseActivity;
import com.lee.library.utils.Constants;

/**
 * @author jv.lee
 */
public class Order_DebugActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_debug);
        Log.e(Constants.TAG, "common/Order_DebugActivity");
    }
}
