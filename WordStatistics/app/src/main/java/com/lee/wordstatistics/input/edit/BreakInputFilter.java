package com.lee.wordstatistics.input.edit;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Author: 陈勇
 * Version: 1.0.0
 * Date: 2016/12/28
 * Mender:
 * Modify:
 * Description: EditText输入过滤器
 */
public class BreakInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if ( "\n".contentEquals(source) || "\r".contentEquals(source)) {
            return "";
        } else if (source.toString().contains("\n")  || source.toString().contains("\r")) {
            String charSequence = source.toString();
            charSequence = charSequence.replaceAll("\n", "");
            charSequence = charSequence.replaceAll("\r", "");
            return charSequence;
        } else {
            return null;
        }
    }
}
