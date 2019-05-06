package com.lee.library.mvp;

import com.lee.library.utils.LogUtil;

/**
 * @author jv.lee
 * @date 2019/5/6
 */
public abstract class BasePresenter<V> implements BaseLifecycleObserver {
    private V mView;
    public int STATUS_TAG = 0;
    public static final int ON_CREATE = 1;
    public static final int ON_START = 2;
    public static final int ON_RESUME = 3;
    public static final int ON_PAUSE = 4;
    public static final int ON_STOP = 5;
    public static final int ON_DESTROY = 6;

    public void onAttachView(V view) {
        mView = view;
    }

    public V getView() {
        return mView;
    }

    @Override
    public void onCrete() {
        LogUtil.i("base presenter onCrete");
        STATUS_TAG = ON_CREATE;
    }

    @Override
    public void onStart() {
        LogUtil.i("base presenter onStart");
        STATUS_TAG = ON_START;
    }

    @Override
    public void onResume() {
        LogUtil.i("base presenter onResume");
        STATUS_TAG = ON_RESUME;
    }

    @Override
    public void onPause() {
        LogUtil.i("base presenter onPause");
        STATUS_TAG = ON_PAUSE;
    }

    @Override
    public void onStop() {
        LogUtil.i("base presenter onStop");
        STATUS_TAG = ON_STOP;
    }

    /**
     * Presenter被销毁时调用
     */
    @Override
    public void onDestroy() {
        LogUtil.i("base presenter onDestroy");
        STATUS_TAG = ON_DESTROY;
        mView = null;
    }

}

