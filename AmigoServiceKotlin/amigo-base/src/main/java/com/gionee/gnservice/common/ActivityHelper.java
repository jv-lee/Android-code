package com.gionee.gnservice.common;

import android.app.Activity;

import com.gionee.gnservice.utils.LogUtil;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ActivityHelper {
    private static final boolean DEBUG = true;
    private static final String TAG = "AppHelper";
    private List<Activity> mAliveActivityList;
    private Activity mCurrentActivity;

    public ActivityHelper() {
        mAliveActivityList = new LinkedList<Activity>();
    }

    public synchronized void onActivityCreate(Activity activity) {
        if (DEBUG) {
            LogUtil.d(TAG, "onCreate activity=" + activity.getClass().getSimpleName());
        }
        if (!mAliveActivityList.contains(activity)) {
            mAliveActivityList.add(activity);
        }
    }

    public synchronized void onActivityResume(Activity activity) {
        if (DEBUG) {
            LogUtil.d(TAG, "onResume activity=" + activity.getClass().getSimpleName());
        }
        this.mCurrentActivity = activity;
    }

    public synchronized void onActivityDestory(Activity activity) {
        if (DEBUG) {
            LogUtil.d(TAG, "onDestory activity=" + activity.getClass().getSimpleName());
        }
        if (mAliveActivityList.contains(activity)) {
            mAliveActivityList.remove(activity);
        }
    }

    public synchronized void onActivityPause(Activity activity) {
        if (DEBUG) {
            LogUtil.d(TAG, "onPause activity=" + activity.getClass().getSimpleName());
        }
        if (mCurrentActivity == activity) {
            mCurrentActivity = null;
        }
    }

    public synchronized void exitAllLiveActivitys() {
        mCurrentActivity = null;
        Iterator<Activity> iterator = mAliveActivityList.iterator();
        while (iterator.hasNext()) {
            iterator.next().finish();
            iterator.remove();
        }
        mAliveActivityList.clear();
        mAliveActivityList = null;
    }

}
