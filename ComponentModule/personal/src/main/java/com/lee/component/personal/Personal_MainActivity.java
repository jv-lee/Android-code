package com.lee.component.personal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lee.library.RecordPathManager;
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
//        //类加载方式
//        try {
//            Class targetClass = Class.forName("com.lee.component.MainActivity");
//            Intent intent = new Intent(this, targetClass);
//            intent.putExtra("name", "lee");
//            startActivity(intent);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        Class<?> targetClass = RecordPathManager.getTargetClass("app", "MainActivity");
        if (targetClass == null) {
            Log.e(Constants.TAG, "获取targetClass为空");
        }
        Intent intent = new Intent(this, targetClass);
        intent.putExtra("name", "lee");
        startActivity(intent);
    }

    public void jumpOrder(View view) {
        //类加载方式
//        try {
//            Class targetClass = Class.forName("com.lee.component.order.Order_MainActivity");
//            Intent intent = new Intent(this, targetClass);
//            intent.putExtra("name", "lee");
//            startActivity(intent);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        Class<?> targetClass = RecordPathManager.getTargetClass("order", "Order_MainActivity");
        if (targetClass == null) {
            Log.e(Constants.TAG, "获取targetClass为空");
        }
        Intent intent = new Intent(this, targetClass);
        intent.putExtra("name", "lee");
        startActivity(intent);
    }

}
