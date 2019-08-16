package com.gionee.gnservice.common.account;

import android.app.Activity;

import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.sdk.IAmigoServiceSdk;

import java.util.Map;

/**
 * Created by caocong on 2/6/17.
 */
public interface IAccountHelper {
    int GET_NAME = 1;
    int GET_UID = 1 << 1;
    int GET_PHOTO = 1 << 2;
    int GET_TOKEN = 1 << 3;
    int GET_LEVEL = 1 << 4;

    //登录
    void login(IAmigoServiceSdk.OnHandleListener listener);

    boolean isLogin();

    //退出
    void loginOut(String userId, IAmigoServiceSdk.OnHandleListener listener);

    //打开个人资料
    void toPersonalInfo(Activity context);

    //排队获取帐号资料
    void getAccountInfo(int flag, String appId, OnGetAccountInfoListener listener);

    void getAccountInfo(int flag, String appId, OnGetAccountInfoListener listener, boolean notifyEveryStep);

    void loadUserInfo(String appId, OnGetAccountInfoListener listener);

    void loadPhoto(String appId, OnGetAccountInfoListener listener);

    void loadMemberLevel(String appId, OnGetAccountInfoListener listener);

    void loadNickName(String appId, OnGetAccountInfoListener listener);

    String getUserId();

    //设置用户profile
    void setUserProfile(String appId, Map<String, String> profileMap, IAmigoServiceSdk.OnHandleListener listener);

    //监听和注销帐号状态变化
    void registerLoginStatusChangeListener(IAmigoServiceSdk.OnLoginStatusChangeListener listener);

    void unRegisterLoginStatusChangeListener(IAmigoServiceSdk.OnLoginStatusChangeListener listener);

    interface OnGetAccountInfoListener {
        void onSuccess(AccountInfo accountInfo);

        void onFail(String errMessage);
    }

}
