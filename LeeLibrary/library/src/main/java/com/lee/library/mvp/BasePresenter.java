package com.lee.library.mvp;

import java.lang.ref.WeakReference;

/**
 * @author jv.lee
 * @date 2019/5/6
 */
public abstract class BasePresenter<V> implements BaseLifecycleObserver {
    private WeakReference<V> mView;
    public int STATUS_TAG = 0;
    public static final int ON_CREATE = 1;
    public static final int ON_START = 2;
    public static final int ON_RESUME = 3;
    public static final int ON_PAUSE = 4;
    public static final int ON_STOP = 5;
    public static final int ON_DESTROY = 6;

    public BasePresenter() {
        bindModel();
    }

    /**
     * 初始化model层
     */
    public abstract void bindModel();

    public void onAttachView(V view) {
        mView = new WeakReference<V>(view);
    }

    public V getView() {
        return mView.get();
    }

    @Override
    public void onCrete() {
        STATUS_TAG = ON_CREATE;
    }

    @Override
    public void onStart() {
        STATUS_TAG = ON_START;
    }

    @Override
    public void onResume() {
        STATUS_TAG = ON_RESUME;
    }

    @Override
    public void onPause() {
        STATUS_TAG = ON_PAUSE;
    }

    @Override
    public void onStop() {
        STATUS_TAG = ON_STOP;
    }

    /**
     * Presenter被销毁时调用
     */
    @Override
    public void onDestroy() {
        STATUS_TAG = ON_DESTROY;
        if (mView != null) {
            mView.clear();
            mView = null;
            System.gc();
        }
    }

}

