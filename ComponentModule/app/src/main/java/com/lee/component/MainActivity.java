package com.lee.component;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lee.component.annotation.ARouter;
import com.lee.component.annotation.Parameter;
import com.lee.component.api.ParameterManager;
import com.lee.component.api.RouterManager;
import com.lee.library.order.OrderDrawable;
import com.lee.library.order.OrderFragmentCall;
import com.lee.library.utils.Constants;

/**
 * @author jv.lee
 */
@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {

    @Parameter
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化模块间传值
        ParameterManager.getInstance().loadParameter(this);
        Log.e(Constants.TAG, "/app/MainActivity:" + username);

        //获取子模块drawable资源
        OrderDrawable drawable = (OrderDrawable) RouterManager.getInstance()
                .build("/order/getDrawable")
                .navigation(this);
        ImageView ivDrawable = findViewById(R.id.iv_drawable);
        ivDrawable.setImageResource(drawable.getDrawable());

        //获取子模块fragment实例
        OrderFragmentCall orderFragmentCall = (OrderFragmentCall) RouterManager.getInstance()
                .build("/order/OrderFragment")
                .navigation(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, orderFragmentCall.getFragment()).commit();
    }

    public void jumpPersonal(View view) {
        RouterManager.getInstance()
                .build("/personal/Personal_MainActivity")
                .withString("username", "jv.lee")
                .navigation(this);
    }

    public void jumpOrder(View view) {
        RouterManager.getInstance()
                .build("/order/Order_MainActivity")
                .withString("username", "jv.lee")
                .navigation(this);
    }
}
