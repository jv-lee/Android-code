package com.gionee.gnservice.module.setting;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.gionee.account.sdk.itf.vo.LoginInfo;
import com.gionee.gnservice.UserCenterActivity;
import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.account.IAccountHelper;
import com.gionee.gnservice.sdk.IAmigoServiceSdk;
import com.gionee.gnservice.utils.LogUtil;

import java.lang.ref.WeakReference;

/**
 * Created by borney on 4/19/17.
 */

public class MemberSettingPresenter implements IMemberSettingContract.Presenter {
    private static final String TAG = "Setting";
    private IAppContext mAppContext;
    private final Activity mActivity;

    public MemberSettingPresenter(@NonNull IMemberSettingContract.View view, @NonNull Activity activity) {
        mAppContext = (IAppContext) activity.getApplicationContext();
        mActivity = activity;
    }

    @Override
    public void logout() {
        if (mActivity == null) {
            return;
        }

        final IAccountHelper accountHelper = mAppContext.accountHelper();
        new Thread(new Runnable() {
            public void run() {
                final String userId = accountHelper.getUserId();
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        accountHelper.loginOut(userId, new LoginOutListener());
                    }
                });
            }
        }).start();
    }

    private class LoginOutListener implements IAmigoServiceSdk.OnHandleListener {
        @Override
        public void onSuccess(LoginInfo loginInfo) {
            LogUtil.i(TAG, "logout success");
            Activity activity = mActivity;
            if (activity == null) {
                LogUtil.i(TAG, "activity is null");
                return;
            }

            Intent intent = new Intent(activity, UserCenterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivityForResult(intent, 0);
        }

        @Override
        public void onFail() {
            LogUtil.i(TAG, "logout fail");
        }

        @Override
        public void onCancel() {

        }
    }

}