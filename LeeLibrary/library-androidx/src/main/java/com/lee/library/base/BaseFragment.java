package com.lee.library.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.lee.library.ioc.InjectManager;

/**
 * @author jv.lee
 */
public abstract class BaseFragment extends Fragment {
    protected BaseActivity mActivity;
    protected FragmentManager mFragmentManager;
    private View mRootView;
    private int mRootResId;
    private boolean isVisibleUser = false;
    private boolean isVisibleView = false;
    private boolean fistVisible = true;

    public BaseFragment() {
    }

    public void setContentView(int resId){
        this.mRootResId = resId;
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
        mFragmentManager = getChildFragmentManager();
        bindData(savedInstanceState);
        bindView();
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
