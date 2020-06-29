package com.lee.library.widget.dialog;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lee.library.R;
import com.lee.library.widget.dialog.core.BaseDialog;

/**
 * @author jv.lee
 * @date 2020-03-07
 * @description 取消确认选择框
 */
public class ChoiceDialog extends BaseDialog {

    private String text;

    public static ChoiceDialog build(Context context, String text) {
        ChoiceDialog dialog = new ChoiceDialog(context);
        dialog.text = text;
        return dialog;
    }

    private ChoiceDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int buildViewId() {
        return R.layout.dialog_choice;
    }

    @Override
    public void bindView() {
        ((TextView) findViewById(R.id.tv_title)).setText(text == null ? "构建WarnDialog text参数不能为空" : text);

        findViewById(R.id.tv_confirm).setOnClickListener(v -> {
            if (confrimListener != null) {
                confrimListener.onConfirm();
            } else {
                dismiss();
            }
        });

        findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onCancel();
            } else {
                dismiss();
            }
        });
    }
}
