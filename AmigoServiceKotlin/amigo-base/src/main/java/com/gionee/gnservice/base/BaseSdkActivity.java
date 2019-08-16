package com.gionee.gnservice.base;

import android.content.Intent;
import android.os.Bundle;

import com.gionee.gnservice.base.webview.BaseWebViewActivity;
import com.gionee.gnservice.sdk.AmigoServiceSdk;
import com.gionee.gnservice.sdk.IAmigoServiceSdk;
import com.gionee.gnservice.sdk.INightModeHelper;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.MiscUtil;
import com.gionee.gnservice.utils.SdkUtil;
import com.gionee.gnservice.utils.ToastUtil;

public abstract class BaseSdkActivity extends BaseActivity implements IAmigoServiceSdk.OnLoginStatusChangeListener, INightModeHelper.OnNightModeChangeListener {
    private static final String TAG = BaseSdkActivity.class.getSimpleName();
    protected boolean mNeedShowNetworkError = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext().accountHelper().registerLoginStatusChangeListener(this);
        if (SdkUtil.isCallBySdk(this)) {
            AmigoServiceSdk.getInstance().nightModeHelper().registerNightModeChangeListener(this);
        }
    }

    @Override
    public void onNightModeChanged(boolean isNightMode) {
        recreate();
    }

    @Override
    public void onGetLoginStatus(boolean isLogin) {
        LogUtil.d(TAG, "onGetLoginStatus isLogin is:" + isLogin);
        if (isLogin) {
            return;
        }

        BaseWebViewActivity.clearCookie(this);
        if (SdkUtil.isCallBySdk(this)) {
            finish();
            return;
        }

        if (MiscUtil.isAppOnForeground(this)) {
            try {
                Intent intent = new Intent(this, Class.forName("com.gionee.gnservice.UserCenterActivity"));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                LogUtil.d(TAG, "onGetLoginStatus e =" + e.toString());
            }
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //LogUtil.d(TAG, "login status is:" + appContext().accountHelper().isLogin());
        if (mNeedShowNetworkError) {
            ToastUtil.showNetWorkErrorToast(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appContext().accountHelper().unRegisterLoginStatusChangeListener(this);
        if (SdkUtil.isCallBySdk(this)) {
            AmigoServiceSdk.getInstance().nightModeHelper().unRegisterNightModeChangeListener(this);
        }
    }

}
