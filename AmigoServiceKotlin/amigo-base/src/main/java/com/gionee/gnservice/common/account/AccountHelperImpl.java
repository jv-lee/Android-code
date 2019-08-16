package com.gionee.gnservice.common.account;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.gionee.account.sdk.itf.GioneeAccount;
import com.gionee.account.sdk.itf.listener.AmiAccResultListener;
import com.gionee.account.sdk.itf.listener.GetUserProfileListener;
import com.gionee.account.sdk.itf.listener.LoginResultListener;
import com.gionee.account.sdk.itf.listener.SetUserProfileListener;
import com.gionee.account.sdk.itf.listener.VerifyListener;
import com.gionee.account.sdk.itf.utils.GioneeUtil;
import com.gionee.account.sdk.itf.vo.LoginInfo;
import com.gionee.account.sdk.itf.vo.Md;
import com.gionee.account.sdk.itf.vo.UrConfig;
import com.gionee.account.sdk.itf.vo.request.RequestLoginVo;
import com.gionee.account.sdk.itf.vo.request.RequestLogoutVo;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.entity.MemberLevelInfo;
import com.gionee.gnservice.sdk.IAmigoServiceSdk;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountHelperImpl implements IAccountHelper {
    private static final String TAG = AccountHelperImpl.class.getSimpleName();
    private GioneeAccount mGioneeAccount;
    private Context mContext;
    private AccountInfo mAccountInfo;
    private List<IAmigoServiceSdk.OnLoginStatusChangeListener> mStatusChangeListeners;
    private List<OnGetAccountInfoListener> mAccountInfoListener;
    private static final int METHOD_GET_USE_INFO = 1;
    private static final int METHOD_GET_PHOTO = 1 << 1;
    private static final int METHOD_GET_NAME = 1 << 2;
    private static final int METHOD_GET_UID = 1 << 3;
    private static final int METHOD_GET_LEVEL = 1 << 4;
    private MethodStep mMethodStep;
    private AccountStatusReceiver mAccountReceiver;

    public AccountHelperImpl(Context context) {
        mContext = context.getApplicationContext();
        mGioneeAccount = GioneeAccount.getInstance(context.getApplicationContext());
        mAccountInfo = new AccountInfo();
        mStatusChangeListeners = new ArrayList<IAmigoServiceSdk.OnLoginStatusChangeListener>();
        mAccountInfoListener = new ArrayList<OnGetAccountInfoListener>();
    }

    @Override
    public void login(final IAmigoServiceSdk.OnHandleListener listener) {
        PreconditionsUtil.checkNotNull(listener);
        try {
            RequestLoginVo requestLoginVo = new RequestLoginVo();
            requestLoginVo.setRequestID(GioneeAccount.START_LOGIN);
            requestLoginVo.setGetToken(true);
            requestLoginVo.setAppid(AppConfig.Account.getAccountAppID());
            requestLoginVo.setNoAssistActivity(false);
            LogUtil.d(TAG, "start Login Activity");
            mGioneeAccount.login(mContext, requestLoginVo, new LoginResultListener() {

                @Override
                public void onSucess(Object o) {
                    LoginInfo info = null;
                    try {
                        info = GioneeUtil.getTnLoginInfo(o);
                        LogUtil.i(TAG, "get login info is:" + info);
                        loginInfo2AccountInfo(info);
                        listener.onSuccess(info);
                    } catch (Exception e) {
                        LogUtil.e(TAG, "parse LoginInfo error, msg: " + e.getMessage());
                        e.printStackTrace();
                        listener.onFail();
                    }
                }

                @Override
                public void onCancel(Object o) {
                    LogUtil.d(TAG, "login onCancel" + o.toString());
                    listener.onCancel();
                }

                @Override
                public void onError(Object o) {
                    LogUtil.e(TAG, "login onerror" + o.toString());
                    listener.onFail();
                }
            });
        } catch (Exception e) {
            LogUtil.e(TAG, "account login exception, msg: " + e.getMessage());
            ToastUtil.showLong(mContext, ResourceUtil.getStringId(mContext, "uc_gionee_account_not_ready"));
        }
    }

    @Override
    public void loginOut(String userId, final IAmigoServiceSdk.OnHandleListener listener) {
        PreconditionsUtil.checkNotNull(listener);
        try {
            LogUtil.d("login out");
            RequestLogoutVo vo = new RequestLogoutVo();
            vo.setUid(userId);
            vo.setNoAssistActivity(true);
            mGioneeAccount.callLoginOut(mContext, userId,
                    new VerifyListener() {

                        @Override
                        public void onSucess(Object o) {
                            listener.onSuccess(null);
                        }

                        @Override
                        public void onCancel(Object o) {
                            listener.onCancel();
                        }
                    });
        } catch (Exception e) {
            LogUtil.e(TAG, "account loginOut exception, msg: " + e.getMessage());
            ToastUtil.showLong(mContext, ResourceUtil.getStringId(mContext, "uc_gionee_account_not_ready"));
        }
    }

    @Override
    public boolean isLogin() {
        boolean isLogin = mGioneeAccount.isAccountLogin();
        LogUtil.i(TAG, "account login status: " + isLogin);
        return isLogin;
    }

    @Override
    public void toPersonalInfo(Activity context) {
        mGioneeAccount.toPersonalActivity(context, new AmiAccResultListener() {
            @Override
            public void onComplete(Object o) {
                boolean result = (Boolean) o;
                if (result) {
                    LogUtil.i(TAG, "logout success.");
                    ToastUtil.showShort(mContext, ResourceUtil.getStringId(mContext, "uc_txt_gn_account_login_success"));
                } else {
                    LogUtil.i(TAG, "logout fail.");
                    ToastUtil.showShort(mContext, ResourceUtil.getStringId(mContext, "uc_txt_gn_account_login_fail"));
                }
            }

            @Override
            public void onError(Object o) {
                int code = (Integer) o;
                if (code == 99001) {
                    LogUtil.i(TAG, "version not match, no logout result.");
                }
            }
        });
    }

    @Override
    public void setUserProfile(String appId, Map<String, String> profileMap, final IAmigoServiceSdk.OnHandleListener listener) {
        PreconditionsUtil.checkNotNull(listener);
        Md md = new Md();
        String gender = profileMap.get("gender");
        md.setGnd(gender.equals("男") ? 1 : 2);
        md.setBid(profileMap.get("birthday"));
        md.setEdu(profileMap.get("degree"));
        md.setJob(profileMap.get("job"));

        String userId = mGioneeAccount.getUserId();
        LogUtil.d(TAG, "appId is:" + appId + "; userId is:" + userId + "; md is:" + md);
        mGioneeAccount.setUserProfile(mContext, appId, userId, md, new SetUserProfileListener() {
                    @Override
                    public void onComplete(Object o) {
                        listener.onSuccess(null);
                    }

                    @Override
                    public void onCancel(Object o) {
                        listener.onCancel();
                    }

                    @Override
                    public void onError(Object o) {
                        LogUtil.d(TAG, o.toString());
                        listener.onFail();
                    }
                }
        );
    }

    @Override
    public void registerLoginStatusChangeListener(final IAmigoServiceSdk.OnLoginStatusChangeListener listener) {
        PreconditionsUtil.checkNotNull(listener);
        mStatusChangeListeners.add(listener);
        try {
            if (mStatusChangeListeners.size() == 1) {
                if (mAccountReceiver == null) {
                    LogUtil.d(TAG, "registerReceiver");
                    mAccountReceiver = new AccountStatusReceiver();
                }
                mAccountReceiver.setOnLoginStatusListener(mOnLoginStatusListener);
                IntentFilter filter = new IntentFilter(AccountStatusReceiver.ACTION);
                mContext.registerReceiver(this.mAccountReceiver, filter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unRegisterLoginStatusChangeListener(final IAmigoServiceSdk.OnLoginStatusChangeListener listener) {
        PreconditionsUtil.checkNotNull(listener);
        if (mStatusChangeListeners.contains(listener)) {
            mStatusChangeListeners.remove(listener);
        }
        try {
            if (mStatusChangeListeners.isEmpty()) {
                LogUtil.d(TAG, "unregisterReceiver");
                mContext.unregisterReceiver(mAccountReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AccountStatusReceiver.OnLoginStatusListener mOnLoginStatusListener = new AccountStatusReceiver.OnLoginStatusListener() {
        @Override
        public void onLogin() {
            LogUtil.d(TAG, "onLogin");
            for (IAmigoServiceSdk.OnLoginStatusChangeListener listener : mStatusChangeListeners) {
                listener.onGetLoginStatus(true);
            }
        }

        @Override
        public void onLoginOut() {
            LogUtil.d(TAG, "onLoginOut");
            for (IAmigoServiceSdk.OnLoginStatusChangeListener listener : mStatusChangeListeners) {
                listener.onGetLoginStatus(false);
            }
            mAccountInfo = new AccountInfo();
        }
    };


    @Override
    public void getAccountInfo(int flag, String appId, OnGetAccountInfoListener listener) {
        getAccountInfo(flag, appId, listener, false);
    }

    public void getAccountInfo(int flag, String appId, OnGetAccountInfoListener listener, boolean notifyEveryStep) {

        PreconditionsUtil.checkNotNull(listener);
        PreconditionsUtil.checkNotNull(appId);
        if (flag == 0) {
            return;
        }
        mMethodStep = new MethodStep(appId, notifyEveryStep);
        mAccountInfoListener.add(listener);
        if ((flag & GET_TOKEN) == GET_TOKEN) {
            mMethodStep.addMethod(METHOD_GET_USE_INFO);
        } else if ((flag & GET_NAME) == GET_NAME || (flag & GET_UID) == GET_UID) {
            if (TextUtils.isEmpty(mAccountInfo.getUserName()) || TextUtils.isEmpty(mAccountInfo.getUserId())) {
                mMethodStep.addMethod(METHOD_GET_USE_INFO);
            }
        }
        if ((flag & GET_PHOTO) == GET_PHOTO) {
            mMethodStep.addMethod(METHOD_GET_PHOTO);
        }
        if ((flag & GET_LEVEL) == GET_LEVEL) {
            mMethodStep.addMethod(METHOD_GET_LEVEL);
        }
        mMethodStep.goNextStep();
    }

    private void getUserInfo(String appId) {
        PreconditionsUtil.checkNotNull(appId);
        LogUtil.d(TAG, "getUserInfo, prepare request");
        RequestLoginVo requestLoginVo = new RequestLoginVo();
        requestLoginVo.setRequestID(GioneeAccount.GET_TOKEN);
        requestLoginVo.setGetToken(true);
        requestLoginVo.setAppid(appId);
        requestLoginVo.setNoAssistActivity(false);
        mGioneeAccount.login(mContext, requestLoginVo, new LoginResultListener() {
            @Override
            public void onSucess(Object o) {
                try {
                    LoginInfo loginInfo = GioneeUtil.getTnLoginInfo(o);
                    LogUtil.i(TAG, "get user info is:" + o.toString());
                    mAccountInfo.setUserName(loginInfo.getName());
                    mAccountInfo.setToken(loginInfo.getToken());
                    mAccountInfo.setUserId(loginInfo.getUid());
                    mMethodStep.goNextStep();
                } catch (Exception e) {
                    LogUtil.e(TAG, "account login error, msg: " + e.getMessage());
                    mMethodStep.throwError(TAG + "get accountinfo fail");
                }
            }

            @Override
            public void onCancel(Object o) {
                LogUtil.d(TAG, "login onCancel" + o.toString());
            }

            @Override
            public void onError(Object o) {
                LogUtil.e(TAG, "get accountInfo error" + o.toString());
                mMethodStep.throwError(TAG + " get accountinfo fail " + o.toString());
            }
        });
    }

    private void getPhoto() {
        LogUtil.d(TAG, "getPhoto() ");
        mGioneeAccount.getCurrentPortrait(mContext.getApplicationContext(), new AmiAccResultListener() {
            @Override
            public void onComplete(Object object) {
                if (object != null && object instanceof Bitmap) {
                    LogUtil.d(TAG, "onComplete() getphote is:" + object);
                    mAccountInfo.setPhoto((Bitmap) object);
                    mMethodStep.goNextStep();
                }
            }

            @Override
            public void onError(Object o) {
                LogUtil.e(TAG, "onError() " + o.toString());
                mMethodStep.goNextStep();
            }
        });
    }

    private void getMemberLevel() {
        LogUtil.d(TAG, "get member level");
        mGioneeAccount.getUrInfo(mContext.getApplicationContext(), new AmiAccResultListener() {
            @Override
            public void onComplete(Object object) {
                try {
                    LoginInfo loginInfo = GioneeUtil.getTnLoginInfo(object);
                    int userLevel = loginInfo.getUr();
                    LogUtil.i(TAG, "get user level is:" + loginInfo.toString());
                    MemberLevel memberLevel = parseMemberLevel(userLevel);
                    memberLevel.setGrowthValue(loginInfo.getGv());
                    List<MemberLevelInfo> memberLevelInfos = urConfig2memberLevelInfo(loginInfo.getUrConfigs());
                    memberLevel.setMemberLevelInfos(memberLevelInfos);
                    mAccountInfo.setMemberLevel(memberLevel);
                    mMethodStep.goNextStep();
                } catch (Exception e) {
                    LogUtil.e(TAG, "getMemberLevel error, msg: " + e.getMessage());
                }
            }

            @Override
            public void onError(Object o) {
                LogUtil.e(TAG, "get member level fail");

                if (mAccountInfo.getMemberLevel() != null) {
                    mMethodStep.goNextStep();
                } else {
                    mMethodStep.throwError(TAG + " get member level fail");
                }
            }
        });
    }

    private List<MemberLevelInfo> urConfig2memberLevelInfo(List<UrConfig> urConfigs) {
        List<MemberLevelInfo> memberLevelInfos = new ArrayList<MemberLevelInfo>();
        if (urConfigs != null) {
            for (UrConfig urConfig : urConfigs) {
                MemberLevelInfo info = new MemberLevelInfo();
                info.setAlisName(urConfig.getAlis());
                info.setMinValue(urConfig.getBeginScore());
                info.setMaxValue(urConfig.getEndScore());
                info.setRank(urConfig.getUserrank());
                memberLevelInfos.add(info);
            }
            LogUtil.d(TAG, "get member level infos is:" + memberLevelInfos.toString());
        }
        return memberLevelInfos;
    }

    private void notifyGetAccountInfo() {
        LogUtil.d(TAG, "notify get account info");
        if (mAccountInfoListener != null && !mAccountInfoListener.isEmpty()) {
            for (OnGetAccountInfoListener listener : mAccountInfoListener) {
                listener.onSuccess(mAccountInfo);
            }
            mAccountInfoListener.clear();
        }
    }

    private void notifyGetAccountInfoFail(String message) {
        if (mAccountInfoListener != null && !mAccountInfoListener.isEmpty()) {
            for (OnGetAccountInfoListener listener : mAccountInfoListener) {
                listener.onFail(message);
            }
            mAccountInfoListener.clear();
        }
    }

    private AccountInfo loginInfo2AccountInfo(LoginInfo logInfo) {
        if (logInfo == null) {
            return null;
        }
        mAccountInfo.setPhoto(logInfo.getPhoto());
        mAccountInfo.setUserName(logInfo.getName());
        mAccountInfo.setUserId(logInfo.getUid());
        mAccountInfo.setToken(logInfo.getToken());
        return mAccountInfo;
    }

    public static MemberLevel parseMemberLevel(int levelNum) {
        switch (levelNum) {
            case 10:
                return MemberLevel.GOLD;
            case 20:
                return MemberLevel.GOLD;
            case 30:
                return MemberLevel.PLATINUM;
            case 40:
                return MemberLevel.DIAMOND;
            case 50:
                return MemberLevel.BLACK_GOLD;
            default:
                return MemberLevel.GOLD;
        }
    }

    private class MethodStep {
        private String appId;
        private int index;
        private List<Integer> methodList = new ArrayList<Integer>();
        private boolean notifyEveryStep = false;

        MethodStep(String appId, boolean notifyEveryStep) {
            this.appId = appId;
            this.index = -1;
            this.notifyEveryStep = notifyEveryStep;
        }

        private boolean hasNextStep() {
            return index < methodList.size() - 1;
        }

        public void throwError(String message) {
            notifyGetAccountInfoFail(message);
        }

        public void goNextStep() {
            LogUtil.d(TAG, "go next step");
            if (!hasNextStep()) {
                notifyGetAccountInfo();
                return;
            }
            index += 1;
            switch (methodList.get(index)) {
                case METHOD_GET_USE_INFO:
                    getUserInfo(appId);
                    break;
                case METHOD_GET_PHOTO:
                    getPhoto();
                    break;
                case METHOD_GET_LEVEL:
                    getMemberLevel();
                    break;
                default:
                    break;
            }
            if (notifyEveryStep && index > 0) {
                notifyGetAccountInfo();
            }
        }

        public void addMethod(int method) {
            methodList.add(method);
        }
    }

    @Override
    public void loadUserInfo(String appId, final OnGetAccountInfoListener listener) {
        LogUtil.i(TAG, "prepare request and call account login");
        RequestLoginVo requestLoginVo = new RequestLoginVo();
        requestLoginVo.setRequestID(GioneeAccount.GET_TOKEN);
        requestLoginVo.setGetToken(true);
        requestLoginVo.setAppid(appId);
        requestLoginVo.setNoAssistActivity(false);
        mGioneeAccount.login(mContext, requestLoginVo, new LoginResultListener() {
            @Override
            public void onSucess(Object o) {
                try {
                    LoginInfo loginInfo = GioneeUtil.getTnLoginInfo(o);
                    LogUtil.d(TAG, "get user info is:" + o.toString());
                    mAccountInfo.setUserName(loginInfo.getName());
                    mAccountInfo.setToken(loginInfo.getToken());
                    mAccountInfo.setUserId(loginInfo.getUid());
                    if (listener != null) {
                        listener.onSuccess(mAccountInfo);
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, "login exception, msg: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel(Object o) {
                LogUtil.i(TAG, "login onCancel" + o.toString());
            }

            @Override
            public void onError(Object o) {
                LogUtil.e(TAG, "get accountInfo error: " + o.toString());
                if (listener != null) {
                    listener.onFail(null);
                }
            }
        });
    }

    @Override
    public void loadPhoto(String appId, final OnGetAccountInfoListener listener) {
        mGioneeAccount.getCurrentPortrait(mContext, new AmiAccResultListener() {
            @Override
            public void onComplete(Object object) {
                if (object != null && object instanceof Bitmap) {
                    LogUtil.d(TAG, "getphote is:" + object);
                    mAccountInfo.setPhoto((Bitmap) object);
                    if (listener != null) {
                        listener.onSuccess(mAccountInfo);
                    }
                }
            }

            @Override
            public void onError(Object o) {
                LogUtil.e(TAG, "get accountInfo error" + o.toString());
                if (listener != null) {
                    listener.onFail(null);
                }
            }
        });
    }

    @Override
    public void loadMemberLevel(String appId, final OnGetAccountInfoListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final LoginInfo loginInfo = mGioneeAccount.getLocalUrInfo(mContext);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (loginInfo != null && listener != null) {
                            listener.onSuccess(mAccountInfo);
                        }
                        mGioneeAccount.getUrInfo(mContext.getApplicationContext(), new AmiAccResultListener() {
                            @Override
                            public void onComplete(Object object) {
                                try {
                                    LoginInfo loginInfo = GioneeUtil.getTnLoginInfo(object);
                                    int userLevel = loginInfo.getUr();
                                    LogUtil.i(TAG, "get user level is:" + loginInfo.toString());
                                    MemberLevel memberLevel = parseMemberLevel(userLevel);
                                    memberLevel.setGrowthValue(loginInfo.getGv());
                                    List<MemberLevelInfo> memberLevelInfos = urConfig2memberLevelInfo(loginInfo.getUrConfigs());
                                    memberLevel.setMemberLevelInfos(memberLevelInfos);
                                    mAccountInfo.setMemberLevel(memberLevel);
                                    if (listener != null) {
                                        listener.onSuccess(mAccountInfo);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Object o) {
                                LogUtil.e(TAG, "get member level fail");
                                if (listener != null) {
                                    listener.onFail(null);
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    @Override
    public void loadNickName(String appId, final OnGetAccountInfoListener listener) {
        LogUtil.d(TAG, "load nick name from gn account");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGioneeAccount.getUserProfile(mContext.getApplicationContext(), AppConfig.Account.getAmigoServiceAppId(), mGioneeAccount.getUserId(), new GetUserProfileListener() {
                    @Override
                    public void onComplete(Object o) {
                        if (o instanceof Md) {
                            Md md = (Md) o;
                            mAccountInfo.setNickName(md.getNim());
                            if (listener != null) {
                                listener.onSuccess(mAccountInfo);
                            }
                        }
                    }

                    @Override
                    public void onCancel(Object o) {

                    }

                    @Override
                    public void onError(Object o) {
                        LogUtil.e(TAG, "loadNickName error: " + o.toString());
                        if (listener != null) {
                            listener.onFail(null);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public String getUserId() {
        return mGioneeAccount.getUserId();
    }

}