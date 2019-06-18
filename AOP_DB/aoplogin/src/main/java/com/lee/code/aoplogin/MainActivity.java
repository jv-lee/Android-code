package com.lee.code.aoplogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lee.code.aoplogin.annotation.ClickBehavior;
import com.lee.code.aoplogin.annotation.LoginCheck;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private final static String TAG = "lee >>>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @ClickBehavior("登陆")
    public void login(View view) {
        Log.e(TAG, "模拟接口请求　验证通过，登陆成功");
    }

    @ClickBehavior("我的专区")
    @LoginCheck
    public void area(View view) {
        Log.e(TAG, "开始跳转到 -> 我的专区 Activity");
        startActivity(new Intent(this, OtherActivity.class));
    }

    @ClickBehavior("我的优惠劵")
    @LoginCheck
    public void coupon(View view) {
        Log.e(TAG, "开始跳转到 -> 我的优惠卷 Activity");
        startActivity(new Intent(this, OtherActivity.class));
    }

    @ClickBehavior("我的积分")
    @LoginCheck
    public void score(View view) {
        Log.e(TAG, "开始跳转到 -> 我的积分 Activity");
        startActivity(new Intent(this, OtherActivity.class));
    }
}
