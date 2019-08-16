package com.gionee.gnservice.sdk;

import android.app.Application;
import android.content.Context;

import com.gionee.account.sdk.itf.vo.LoginInfo;

/**
 * Created by caocong on 5/9/17.
 */
public interface IAmigoServiceSdk {

    /**
     * 在application中添加
     */
    void init(Application application);

    /**
     * 实时刷新数据
     */
    void onResume(AmigoServiceCardView cardView);

    /**
     * 当前rom中是否有最新版的用户中心
     */
    boolean hasSupportAmigoServiceVersion(Context context);

    /**
     * @param appId     正式环境下appId;
     * @param appIdTest 测试环境下appId;
     */
    void setAppId(String appId, String appIdTest);

    /**
     * 设置为夜间模式
     *
     * @param isNightMode true为夜间模式，false为正常模式
     */
    void setNightMode(boolean isNightMode);

    /**
     * 设置支持省电模式
     */
    void setSupportPowerMode(boolean supportPowerMode);

    interface OnLoginStatusChangeListener {
        void onGetLoginStatus(boolean isLogin);
    }

    /**
     * 注册帐号状态
     */
    void registerLoginStatusChangeListener(OnLoginStatusChangeListener listener);


    void unRegisterLoginStatusChangeListener(OnLoginStatusChangeListener listener);

    /**
     * 跳转到登录页面
     */
    void toLoginRegisterActivity(Context context, OnHandleListener listener);

    interface OnHandleListener {

        void onSuccess(LoginInfo loginInfo);

        void onFail();

        void onCancel();
    }

    void toIntegralActivity(Context context);

    void toAcoinActivity(Context context);

    void toCouponActivity(Context context);

    void toIntegralMallActivity(Context context);

    void toIntegralLotteryActivity(Context context);


}
