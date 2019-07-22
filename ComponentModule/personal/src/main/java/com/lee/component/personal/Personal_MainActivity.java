package com.lee.component.personal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lee.component.annotation.ARouter;
import com.lee.component.api.RouterManager;
import com.lee.library.base.BaseActivity;
import com.lee.library.utils.Constants;

/**
 * @author jv.lee
 */
@ARouter(path = "/personal/Personal_MainActivity")
public class Personal_MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_main);
        Log.e(Constants.TAG, "common/Personal_MainActivity");
    }

    public void jumpApp(View view) {
        RouterManager.getInstance()
                .build("/app/MainActivity")
                .navigation(this);
    }

    public void jumpOrder(View view) {
        RouterManager.getInstance()
                .build("/order/Order_MainActivity")
                .navigation(this);
    }

}
