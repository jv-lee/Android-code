package com.lee.component.order;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lee.component.annotation.ARouter;
import com.lee.component.annotation.Parameter;
import com.lee.component.api.ParameterManager;
import com.lee.component.api.RouterManager;
import com.lee.library.base.BaseActivity;
import com.lee.library.utils.Constants;

/**
 * @author jv.lee
 */
@ARouter(path = "/order/Order_MainActivity")
public class Order_MainActivity extends BaseActivity {

    @Parameter
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_main);
        ParameterManager.getInstance().loadParameter(this);
        Log.e(Constants.TAG, "/order/Order_MainActivity:"+username);
    }


    public void jumpApp(View view) {
        RouterManager.getInstance()
                .build("/app/MainActivity")
                .withString("username","jv.lee")
                .navigation(this);
    }

    public void jumpPersonal(View view) {
        RouterManager.getInstance()
                .build("/personal/Personal_MainActivity")
                .withString("username","jv.lee")
                .navigation(this);

    }

}
