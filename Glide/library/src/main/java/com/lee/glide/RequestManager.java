package com.lee.glide;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lee.glide.lifecycle.ActivityLifecycle;
import com.lee.glide.lifecycle.FragmentActivityLifecycle;

/**
 * @author jv.lee
 * @date 2019/9/2.
 * @description 图片请求管理类
 */
public class RequestManager {

    private final String FRAGMENT_ACTIVITY_NAME = "Fragment_Activity_Name";
    private final String ACTIVITY_NAME = "Activity_Name";
    private final int NEXT_HANDLER_MSG = 995000;

    private Context requestManagerContext;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    private static RequestTargetEngine requestTargetEngine;

    //构造代码块 无需在每一个构造方法中去统一写入了
    {
        if (requestTargetEngine == null) {
            requestTargetEngine = new RequestTargetEngine();
        }
    }

    RequestManager(Activity activity) {
        this.requestManagerContext = activity;

        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.Fragment fragment = fragmentManager.findFragmentByTag(ACTIVITY_NAME);
        if (fragment == null) {
            fragment = new ActivityLifecycle(requestTargetEngine);
        }
        if (fragment.isAdded()) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.add(fragment, ACTIVITY_NAME).commitAllowingStateLoss();

        mHandler.sendEmptyMessage(NEXT_HANDLER_MSG);
    }

    RequestManager(FragmentActivity fragmentActivity) {
        this.requestManagerContext = fragmentActivity;

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_ACTIVITY_NAME);
        if (fragment == null) {
            fragment = new FragmentActivityLifecycle(requestTargetEngine);
        }
        if (fragment.isAdded()) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.add(fragment, FRAGMENT_ACTIVITY_NAME).commitAllowingStateLoss();

        mHandler.sendEmptyMessage(NEXT_HANDLER_MSG);
    }

    RequestManager(Context context) {
        this.requestManagerContext = context;
    }

    /**
     * 拿到要显示的图片路径
     *
     * @param path
     * @return
     */
    RequestTargetEngine load(String path) {
        //移除Handler
        mHandler.removeMessages(NEXT_HANDLER_MSG);
        return requestTargetEngine;
    }


}
