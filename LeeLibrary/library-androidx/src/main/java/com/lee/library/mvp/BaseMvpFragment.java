package com.lee.library.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.lee.library.base.BaseFragment;

/**
 * @author jv.lee
 * @date 2019/5/6
 */

public abstract class BaseMvpFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MVPManager.injectPresenter(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
