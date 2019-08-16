package com.gionee.gnservice.common.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by caocong on 7/31/17.
 */
public class AccountStatusReceiver extends BroadcastReceiver {
    private static final String TAG = AccountStatusReceiver.class.getSimpleName();
    public static final String ACTION = "com.gionee.account.broadcast.loginresult";
    private OnLoginStatusListener mListener;

    public AccountStatusReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String loginResult = intent.getExtras().getString("login_result");
            String uid = intent.getExtras().getString("u");
            if ("login".equals(loginResult)) {
                notifyLogin();
            } else {
                notifyLoginOut();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnLoginStatusListener(OnLoginStatusListener listener) {
        this.mListener = listener;
    }

    private void notifyLogin() {
        if (mListener!=null){
            mListener.onLogin();
        }
    }

    private void notifyLoginOut() {
        if (mListener!=null){
            mListener.onLoginOut();
        }
    }

    public interface OnLoginStatusListener {
        void onLogin();
        void onLoginOut();
    }

}
