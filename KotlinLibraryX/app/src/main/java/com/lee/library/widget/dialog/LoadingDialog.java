package com.lee.library.widget.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lee.library.R;
import com.lee.library.widget.dialog.core.BaseTranslucentDialog;

/**
 * @author jv.lee
 * @date 2020-03-07
 * @description
 */
public class LoadingDialog extends BaseTranslucentDialog {

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int buildViewId() {
        return R.layout.widget_dialog_loading;
    }

    @Override
    protected void bindView() {
    }
}
