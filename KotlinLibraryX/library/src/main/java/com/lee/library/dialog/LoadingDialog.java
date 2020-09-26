package com.lee.library.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lee.library.R;
import com.lee.library.dialog.core.BaseTranslucentDialog;

/**
 * @author jv.lee
 * @date 2020-03-07
 * @description
 */
public class LoadingDialog extends BaseTranslucentDialog {

    public LoadingDialog(@NonNull Context context) {
        super(context,false);
    }

    @Override
    protected int buildViewId() {
        return R.layout.layout_dialog_loading;
    }

    @Override
    protected void bindView() {
    }
}
