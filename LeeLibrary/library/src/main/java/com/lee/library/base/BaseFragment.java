package com.lee.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lee.library.ioc.InjectManager;

public abstract class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected BaseActivity mActivity;
    protected View mRootView;
    protected int mRootResId;
    protected boolean isVisibleUser = false;
    protected boolean isVisibleView = false;
    protected boolean fistVisible = true;

    public BaseFragment() {
    }

    public void setContentView(int ResId){
        this.mRootResId = ResId;
    }

    public <T extends View> T findViewById(int id) {
        return (T) mRootView.findViewById(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        InjectManager.injectLayout(this);
        mRootView = inflater.inflate(mRootResId,container,false);
        InjectManager.injectViews(this);
        InjectManager.injectEvents(this);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        bindData(savedInstanceState);
        isVisibleView = true;
        if (isVisibleView && isVisibleUser && fistVisible) {
            fistVisible = false;
            lazyLoad();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisibleUser = true;
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    protected abstract void bindData(Bundle savedInstanceState);

    protected abstract void lazyLoad();
}
