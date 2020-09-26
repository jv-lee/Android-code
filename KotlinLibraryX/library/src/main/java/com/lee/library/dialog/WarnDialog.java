package com.lee.library.dialog;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lee.library.R;
import com.lee.library.dialog.core.BaseAlertDialog;

/**
 * @author jv.lee
 * @date 2020-03-07
 * @description 警告提示dialog
 */
public class WarnDialog extends BaseAlertDialog {

    private String titleText;

    public static WarnDialog build(Context context, String text) {
        WarnDialog dialog = new WarnDialog(context);
        dialog.titleText = text;
        return dialog;
    }

    private WarnDialog(@NonNull Context context) {
        super(context,false);
    }

    @Override
    public int buildViewId() {
        return R.layout.layout_dialog_warm;
    }

    @Override
    public void bindView() {
        ((TextView) findViewById(R.id.tv_title)).setText(titleText == null ? "构建WarnDialog text参数不能为空" : titleText);

        findViewById(R.id.tv_confirm).setOnClickListener(v -> {
            if (confirmListener != null) {
                confirmListener.onConfirm();
            } else {
                dismiss();
            }
        });

    }

}
