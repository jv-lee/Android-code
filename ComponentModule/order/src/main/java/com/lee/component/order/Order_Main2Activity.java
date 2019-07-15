package com.lee.component.order;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lee.component.annotation.ARouter;

/**
 * @author jv.lee
 */
@ARouter(path = "/order/Order_Main2Activity")
public class Order_Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_main2);
    }
}
