package com.lee.library.widget.interadap;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author jv.lee
 * @date 2020/4/24
 * @description
 */
public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

}
