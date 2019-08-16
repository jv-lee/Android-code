package com.gionee.gnservice.sdk;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.config.EnvConfig;
import com.gionee.gnservice.sdk.cardview.IMemberCardViewContract;
import com.gionee.gnservice.sdk.cardview.MemberCardViewPresenter;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;
import com.gionee.gnservice.utils.SdkUtil;

public final class AmigoServiceSdk implements IAmigoServiceSdk {
    private static final String TAG = AmigoServiceSdk.class.getSimpleName();

    private static class Holder {
        private static AmigoServiceSdk sInstance = new AmigoServiceSdk();
    }

    private IAppContext mAppContext;

    //夜间模式
    private boolean mIsNightMode = false;

    private INightModeHelper mNightModeHelper;

    //是否支持省电模式
    private boolean mSupportPowerMode = true;

    private boolean sInit = false;

    public static AmigoServiceSdk getInstance() {
        return Holder.sInstance;
    }

    private AmigoServiceSdk() {
        mNightModeHelper = new NightModeHelper();
    }

    public void init(Application application) {
        PreconditionsUtil.checkNotNull(application);
        mAppContext = new AppContextImpl(application);
        LogUtil.initTag(application);
        EnvConfig.init(application);
        sInit = true;
    }

    @Override
    public void onResume(AmigoServiceCardView cardView) {
        PreconditionsUtil.checkNotNull(cardView);
        checkInit();
        IMemberCardViewContract.Presenter presenter = new MemberCardViewPresenter(mAppContext, cardView);
        presenter.loadAccountInfo();
    }

    @Override
    public boolean hasSupportAmigoServiceVersion(Context context) {
        return SdkUtil.hasSupportAmigoServiceVersion(context);
    }

    @Override
    public void setAppId(String appId, String appIdTest) {
        AppConfig.Account.setAppID(appId, appIdTest);
    }

    @Override
    public void setNightMode(boolean isNightMode) {
        LogUtil.d(TAG, "set night mode is" + isNightMode);
        if (mIsNightMode != isNightMode) {
            ((NightModeHelper) mNightModeHelper).notifyChanged(isNightMode);
        }
        mIsNightMode = isNightMode;
    }

    @Override
    public void setSupportPowerMode(boolean supportPowerMode) {
        mSupportPowerMode = supportPowerMode;
    }

    public boolean isSupportPowerMode() {
        return mSupportPowerMode;
    }


    public boolean isNightMode() {
        return mIsNightMode;
    }

    @Override
    public void registerLoginStatusChangeListener(IAmigoServiceSdk.OnLoginStatusChangeListener listener) {
        checkInit();
        mAppContext.accountHelper().registerLoginStatusChangeListener(listener);
    }

    @Override
    public void unRegisterLoginStatusChangeListener(OnLoginStatusChangeListener listener) {
        checkInit();
        mAppContext.accountHelper().unRegisterLoginStatusChangeListener(listener);
    }

    @Override
    public void toLoginRegisterActivity(Context context, OnHandleListener listener) {
        checkInit();
        mAppContext.accountHelper().login(listener);
    }

    @Override
    public void toIntegralActivity(Context context) {
        toActivity(context, "com.gionee.gnservice.sdk.toMemberIntegralActivity", "com.gionee.gnservice.sdk.integral.MemberIntegralActivity");
    }

    @Override
    public void toAcoinActivity(Context context) {
        Intent intent = new Intent("com.gionee.gsp.wallet.components.activities.WalletMainActivity");
        intent.putExtra("app_id", AppConfig.Account.AMIGO_SERVICE_APP_ID);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    @Override
    public void toCouponActivity(Context context) {
        toActivity(context, "com.gionee.gnservice.sdk.toMemberCouponActivity", "com.gionee.gnservice.sdk.coupon.MemberCouponActivity");
    }

    @Override
    public void toIntegralMallActivity(Context context) {
        toActivity(context, "com.gionee.gnservice.sdk.toMemberIntegralMallActivity", "com.gionee.gnservice.sdk.integral.MemberIntegralMallActivity");
    }

    @Override
    public void toIntegralLotteryActivity(Context context) {
        toActivity(context, "com.gionee.gnservice.sdk.toMemberIntegralLotteryActivity", "com.gionee.gnservice.sdk.integral.MemberIntegralLotteryActivity");
    }

    private void toActivity(Context context, String action, String className) {
        Intent intent = new Intent();
        if (SdkUtil.isSdkRomVersion()) {
            LogUtil.d(TAG, "from rom version sdk");
            intent.setAction(action);
            intent.setPackage(SdkUtil.AMIGO_SERVICE_PACKAGE_NAME);
        } else {
            LogUtil.d(TAG, "from common rom version sdk");
            intent.setClassName(context, className);
            intent.setPackage(context.getPackageName());
        }
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }


    public IAppContext appContext() {
        checkInit();
        return mAppContext;
    }

    private void checkInit() {
        if (!sInit) {
            throw new RuntimeException("AmigoServiceSdk should call init in Application");
        }
    }

    public INightModeHelper nightModeHelper() {
        return mNightModeHelper;
    }
}
