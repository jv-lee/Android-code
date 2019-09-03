package com.gionee.simple;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.sdk.AmigoServiceSdk;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.SdkUtil;

public abstract class BaseFragment extends Fragment {
    protected String TAG = this.getClass().getSimpleName();
    protected Activity mActivity;
    protected View mRootView;
    private boolean isVisibleUser = false;
    private boolean isVisibleView = false;
    private boolean fistVisible = true;


    protected IAppContext mAppContext;

    private void initAppContext() {
        if (SdkUtil.isCallBySdk(getActivity())) {
            mAppContext = AmigoServiceSdk.getInstance().appContext();
        } else {
            mAppContext = (IAppContext) getActivity().getApplication();
        }
        LogUtil.d(TAG, "init Appcontext is null:" + (mAppContext == null));
    }

    protected IAppContext appContext() {
        initAppContext();
        return mAppContext;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getV(int id) {
        return (T) mRootView.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getV(View view, int id) {
        return (T) view.findViewById(id);
    }

    public BaseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resId = bindRootView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(resId, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        Log.i(TAG, "onActivityCreated: onActivityCreated");
        bindData();
        isVisibleView = true;
        if (isVisibleUser && fistVisible) {
            fistVisible = false;
            lazyLoad();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisibleUser = true;
            Log.i(TAG, "onActivityCreated: setUserVisibleHint");
            onFragmentResume();
            //首次用户可见 开始加载数据
            if (isVisibleView && isVisibleUser && fistVisible) {
                fistVisible = false;
                lazyLoad();
            }
        } else {
            isVisibleUser = false;
            onFragmentPause();
        }
    }

    protected void onFragmentResume() {
    }

    protected void onFragmentPause() {
    }

    protected abstract int bindRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 设置加载数据等业务操作
     *
     */
    protected abstract void bindData();

    /**
     * 使用page 多fragment时 懒加载
     */
    protected void lazyLoad() {
    }



}
