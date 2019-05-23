package com.lee.library.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lee.library.ioc.InjectManager;

/**
 * @author jv.lee
 */
public abstract class BaseFragment extends Fragment {
    protected BaseActivity mActivity;
    protected FragmentManager mFragmentManager;
    private View mRootView;
    private boolean isVisibleUser = false;
    private boolean isVisibleView = false;
    private boolean fistVisible = true;

    public BaseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = InjectManager.injectLayout(this);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        mFragmentManager = getChildFragmentManager();
        super.onActivityCreated(savedInstanceState);
        InjectManager.injectViews(this);
        bindData(savedInstanceState);
        bindView();
        InjectManager.injectEvents(this);
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

    /**
     * 设置加载数据等业务操作
     * @param savedInstanceState 重置回调参数
     */
    protected abstract void bindData(Bundle savedInstanceState);

    /**
     * 设置view基础配置
     */
    protected abstract void bindView();

    /**
     * 使用page 多fragment时 懒加载
     */
    protected abstract void lazyLoad();
}
