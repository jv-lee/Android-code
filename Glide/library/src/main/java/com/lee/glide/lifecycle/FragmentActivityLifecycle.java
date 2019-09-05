package com.lee.glide.lifecycle;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;

/**
 * @author jv.lee
 * @date 2019/9/2.
 * @description 生命周期监听fragment
 */
public class FragmentActivityLifecycle extends Fragment {

    private LifycycleCallback lifycycleCallback;

    public FragmentActivityLifecycle() {
    }

    @SuppressLint("ValidFragment")
    public FragmentActivityLifecycle(LifycycleCallback lifycycleCallback) {
        this.lifycycleCallback = lifycycleCallback;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (lifycycleCallback != null) {
            lifycycleCallback.glideInitAction();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (lifycycleCallback != null) {
            lifycycleCallback.glideStopAction();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lifycycleCallback != null) {
            lifycycleCallback.glideRecycleAction();
        }
    }

}