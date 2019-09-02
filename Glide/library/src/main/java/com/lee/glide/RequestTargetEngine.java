package com.lee.glide;

import android.util.Log;
import android.widget.ImageView;

import com.lee.glide.lifecycle.LifycycleCallback;

/**
 * @author jv.lee
 * @date 2019/9/2.
 * @description 请求资源 引擎
 */
public class RequestTargetEngine implements LifycycleCallback {

    private static final String TAG = "RequestTargetEngine";

    @Override
    public void glideInitAction() {
        Log.i(TAG, "glideInitAction: glide生命周期初始化");
    }

    @Override
    public void glideStopAction() {
        Log.i(TAG, "glideInitAction: glide生命周期停止");
    }

    @Override
    public void glideRecycleAction() {
        Log.i(TAG, "glideInitAction: glide生命周期回收");
    }

    void into(ImageView imageView) {

    }
}
