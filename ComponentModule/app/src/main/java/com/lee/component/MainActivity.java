package com.lee.component;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lee.component.annotation.ARouter;
import com.lee.component.annotation.model.RouterBean;
import com.lee.component.api.core.ARouterLoadGroup;
import com.lee.component.api.core.ARouterLoadPath;
import com.lee.component.test.ARouter$$Group$$order;
import com.lee.component.test.ARouter$$Group$$personal;

import java.util.Map;

/**
 * @author jv.lee
 */
@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpPersonal(View view) {
        ARouterLoadGroup loadGroup = new ARouter$$Group$$personal();
        Map<String, Class<? extends ARouterLoadPath>> groupMap = loadGroup.loadGroup();
        Class<? extends ARouterLoadPath> clazz = groupMap.get("personal");
        try {
            ARouterLoadPath path = clazz.newInstance();
            Map<String, RouterBean> pathMap = path.loadPath();
            //获取order/Order_MainActivity
            RouterBean routerBean = pathMap.get("/personal/Personal_MainActivity");
            if (routerBean != null) {
                startActivity(new Intent(this, routerBean.getClazz()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void jumpOrder(View view) {
        ARouterLoadGroup loadGroup = new ARouter$$Group$$order();
        Map<String, Class<? extends ARouterLoadPath>> groupMap = loadGroup.loadGroup();
        Class<? extends ARouterLoadPath> clazz = groupMap.get("order");
        try {
            ARouterLoadPath path = clazz.newInstance();
            Map<String, RouterBean> pathMap = path.loadPath();
            //获取order/Order_MainActivity
            RouterBean routerBean = pathMap.get("/order/Order_MainActivity");
            if (routerBean != null) {
                startActivity(new Intent(this, routerBean.getClazz()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
