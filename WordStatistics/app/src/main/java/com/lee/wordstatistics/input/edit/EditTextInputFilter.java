package com.lee.wordstatistics.input.edit;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;


/**
 * Author: 陈勇
 * Version: 1.0.0
 * Date: 2016/12/28
 * Mender:
 * Modify:
 * Description: EditText输入过滤器
 */
public class EditTextInputFilter implements InputFilter {

    private Context mContext;
    /**
     * 输入字符的最大长度，字母长度为1，汉字为2
     * 如：mMaxLength = 30 表示最大长度为15个汉字或30个字母
     */
    private int mMaxLength;
    /**
     * 输入长度达到最大值时的提示文字
     */
    private String mToastMessage;
    /**
     * 输入剩余长度回调
     */
    private IInputRemainLengthListener mInputRemainLengthListener = null;


    public EditTextInputFilter(Context context, int maxLength, String toastMessage) {
        mContext = context;
        mMaxLength = maxLength;
        mToastMessage = toastMessage;
    }

    public int getMaxLength() {
        return mMaxLength;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int destLen = 0;    // 已输入字符串的长度
        for (int i = 0; i < dest.length(); i++) {
            String subStr = dest.toString().substring(i, i + 1);
            destLen += subStr.getBytes().length > 1 ? 2 : 1;
        }

        int sourceLen = 0;  // 输入字符串的长度
        for (int i = 0; i < source.length(); i++) {
            String subStr = source.toString().substring(i, i + 1);
            sourceLen += subStr.getBytes().length > 1 ? 2 : 1;
        }

        if (destLen >= mMaxLength && sourceLen > 0) {   // 已输入字符串的长度 >= 最大长度 且 有输入时
            if (!TextUtils.isEmpty(mToastMessage)) {
//                ToastUtils.showToast(mContext, mToastMessage);
                Log.i("jv.lee", "filter: " + mToastMessage);
            }
            if (mInputRemainLengthListener != null) {
                mInputRemainLengthListener.onRemainLength(0);
            }
            return "";
        } else if (destLen + sourceLen <= mMaxLength) {    // 已输入字符串的长度 + 输入字符串的长度 <= 最大长度
            if (mInputRemainLengthListener != null) {
                if (sourceLen > 0) {
                    mInputRemainLengthListener.onRemainLength(mMaxLength - (destLen + sourceLen));
                } else {
                    int deleteDestLen = 0;    // 删除已输入字符串的长度
                    for (int i = dstart; i < dend; i++) {
                        String subStr = dest.toString().substring(i, i + 1);
                        deleteDestLen += subStr.getBytes().length > 1 ? 2 : 1;
                    }

                    mInputRemainLengthListener.onRemainLength(mMaxLength - (destLen + sourceLen) + deleteDestLen);
                }
            }
            return null; // keep original
        } else {    // (已输入字符串的长度 < 最大长度) && (已输入字符串的长度 + 输入字符串的长度 > 最大长度)
            if (!TextUtils.isEmpty(mToastMessage)) {
                Log.i("jv.lee", "filter: " + mToastMessage);
//                ToastUtils.showToast(mContext, mToastMessage);
            }
            if (mInputRemainLengthListener != null) {
                mInputRemainLengthListener.onRemainLength(0);
            }

            int subEnd = start;    // 输入字符串的结束位置
            int keepLen = 0;       // 输入字符串可以输入的长度
            int remainLen = mMaxLength - destLen;   // 剩余的总长度
            for (int i = 0; (i < remainLen) && (keepLen < remainLen); i++) {
                String subStr = source.toString().substring(i, i + 1);
                int len = subStr.getBytes().length > 1 ? 2 : 1;
                if (len <= remainLen - keepLen) {    // 可以输入subStr时subEnd++，注意keepLen增加的长度
                    if (subStr.getBytes().length > 1) {
                        keepLen += 2;
                    } else {
                        keepLen++;
                    }
                    subEnd++;
                } else {    // 不可以输入subStr了
                    break;
                }
            }
            return source.subSequence(start, subEnd);
        }
    }

    public static int getLength(String str) {
        int totalLength = 0;
        for (int i = 0; i < str.length(); i++) {
            String subStr = str.substring(i, i + 1);
            int len = subStr.getBytes().length > 1 ? 2 : 1;
            totalLength += len;
        }
        return totalLength;
    }


    /**
     * 设置输入剩余长度回调接口
     *
     * @param listener
     */
    public void setInputRemainLengthListener(IInputRemainLengthListener listener) {
        mInputRemainLengthListener = listener;
    }

    /**
     * 输入剩余长度回调接口
     */
    public interface IInputRemainLengthListener {

        /**
         * 输入剩余长度回调
         *
         * @param remainLength 剩余长度，字母长度为1，汉字为2
         */
        void onRemainLength(int remainLength);
    }
}
