package com.gionee.gnservice.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Process;

import com.gionee.gnservice.common.ActivityHelper;

public class BaseApplication extends Application {
    private ActivityHelper mActivityHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityHelper = new ActivityHelper();
        registerActivityLifecycleCallbacks(mActivityLifecycleCallback);
    }

    public void exit() {
        mActivityHelper.exitAllLiveActivitys();
        unregisterActivityLifecycleCallbacks(mActivityLifecycleCallback);
        Process.killProcess(Process.myPid());
    }

    private ActivityLifecycleCallbacks mActivityLifecycleCallback = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mActivityHelper.onActivityCreate(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            mActivityHelper.onActivityResume(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            mActivityHelper.onActivityPause(activity);
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            mActivityHelper.onActivityDestory(activity);
        }
    };

}
