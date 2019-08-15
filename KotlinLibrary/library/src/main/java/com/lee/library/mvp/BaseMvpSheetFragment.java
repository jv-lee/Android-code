package com.lee.library.mvp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lee.library.base.BaseFragment;
import com.lee.library.base.BaseSheetFragment;

/**
 * @author jv.lee
 * @date 2019/5/6
 */
public abstract class BaseMvpSheetFragment extends BaseSheetFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MvpManager.injectPresenter(this);
        return super.onCreateDialog(savedInstanceState);
    }

}
