package com.lee.library.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lee.library.base.BaseDialogFragment;

/**
 * @author jv.lee
 * @date 2019/5/6
 */
public abstract class BaseMvpDialogFragment extends BaseDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MvpManager.injectPresenter(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
