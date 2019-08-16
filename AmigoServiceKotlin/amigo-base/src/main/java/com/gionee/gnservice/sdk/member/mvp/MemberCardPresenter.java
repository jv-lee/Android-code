package com.gionee.gnservice.sdk.member.mvp;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.account.AccountHelperImpl;
import com.gionee.gnservice.common.account.IAccountHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.domain.AcoinCase;
import com.gionee.gnservice.domain.CouponCase;
import com.gionee.gnservice.domain.IntegralCase;
import com.gionee.gnservice.domain.MemberPrivilegeCase;
import com.gionee.gnservice.domain.Observer;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.IntegralInterface;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

import java.lang.ref.WeakReference;

/**
 * Created by caocong on 1/6/17.
 */
public class MemberCardPresenter implements IMemberCardContract.Presenter {
    private static final String TAG = MemberCardPresenter.class.getSimpleName();
    private IAppContext mAppContext;
    private WeakReference<IMemberCardContract.View> mViewWeakReference;
    private AcoinCase mAcoinCase;
    private MemberPrivilegeCase mMemberPrivilegeCase;
    private IntegralCase mIntegralCase;
    private CouponCase mCouponCase;
    private IAccountHelper mAccountHelper;

    public MemberCardPresenter(IAppContext appContext, IMemberCardContract.View view) {
        PreconditionsUtil.checkNotNull(appContext);
        PreconditionsUtil.checkNotNull(view);
        this.mAppContext = appContext;
        mViewWeakReference = new WeakReference<IMemberCardContract.View>(view);
        mMemberPrivilegeCase = new MemberPrivilegeCase(appContext);
        mIntegralCase = new IntegralCase(appContext);
        mCouponCase = new CouponCase(appContext);
        mAccountHelper = mAppContext.accountHelper();
    }


    private void loadACoinBalance(String userId) {
        if (mAcoinCase == null) {
            mAcoinCase = new AcoinCase(mAppContext);
        }
        mAcoinCase.getACoinBalance(userId, new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.e(TAG, "get A coin error");
            }

            @Override
            public void onNext(String coin) {
                IMemberCardContract.View view = mViewWeakReference.get();
                if (view != null) {
                    view.showACoinBalance(coin);
                }
            }
        });
    }

    private void loadPrivilegesSize(MemberLevel memberLevel) {
        mMemberPrivilegeCase.getMemberPrivilegesSize(memberLevel, new Observer<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.e(TAG, "loadPrivilegesSize error");
            }

            @Override
            public void onNext(Integer value) {
                IMemberCardContract.View view = mViewWeakReference.get();
                if (view != null) {
                    view.showPrivilegesSize(value);
                }
            }
        });
    }

    private void loadUserIntegral(String token) {
        mIntegralCase.getIntegralUser(token, new Observer<IntegralInterface>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.e(TAG, "loadUserIntegral error");
            }

            @Override
            public void onNext(IntegralInterface value) {
                IMemberCardContract.View view = mViewWeakReference.get();
                if (view != null && value != null) {
                    LogUtil.i(TAG, "loadUserIntegral success, integral = " + value.getIntegralValue());
                    view.showUserIntegral(value.getIntegralValue());
                    loadMemberLevel(value.getRankValue());
                }
            }
        });
    }

    private void loadCouponNumber(String userId) {
        mCouponCase.getCouponsUseableSize(userId, new Observer<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.e(TAG, "loadCouponNumber error");
            }

            @Override
            public void onNext(Integer value) {
                IMemberCardContract.View view = mViewWeakReference.get();
                if (view != null) {
                    view.showCouponNumber(value);
                }
            }
        });
    }

    @Override
    public void loadAccountInfo() {
        LogUtil.d(TAG, "load Account Info");
        IMemberCardContract.View view = mViewWeakReference.get();
        if (view == null) {
            LogUtil.d(TAG, "view is recycle!!!");
            return;
        }
        loadUserInfo();
        loadPhoto();
        //loadMemberLevel();
    }

    private void loadUserInfo() {
        mAccountHelper.loadUserInfo(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo info) {
                LogUtil.i(TAG, "loadUserInfo callback success");
                IMemberCardContract.View view = mViewWeakReference.get();
                if (view != null && info != null) {
                    loadUserIntegral(info.getToken());
                    loadACoinBalance(info.getUserId());
                    loadCouponNumber(info.getUserId());
                    loadNickName(info);
                }
            }

            @Override
            public void onFail(String errMessage) {
                LogUtil.e(TAG, "loadUserInfo fail, msg = " + errMessage);
            }
        });
    }

    private void loadNickName(final AccountInfo info) {
        mAccountHelper.loadNickName(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                LogUtil.d(TAG, "loadNickName success");
                info.setNickName(accountInfo.getNickName());
                IMemberCardContract.View view = mViewWeakReference.get();
                if (view != null && info != null) {
                    view.showAccountView(info);
                }
            }

            @Override
            public void onFail(String errMessage) {
                LogUtil.e(TAG, "loadNickName fail, msg = " + errMessage);
            }
        });
    }

    private void loadPhoto() {
        mAccountHelper.loadPhoto(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo info) {
                LogUtil.d(TAG, "loadPhoto success");
                IMemberCardContract.View view = mViewWeakReference.get();
                if (view != null && info != null) {
                    view.showPhoto(info.getPhoto());
                }
            }

            @Override
            public void onFail(String errMessage) {
                LogUtil.e(TAG, "loadPhoto fail, msg = " + errMessage);
            }
        });
    }

    private void loadMemberLevel(int rank) {
        MemberLevel level = AccountHelperImpl.parseMemberLevel(rank);
        IMemberCardContract.View view = mViewWeakReference.get();
        if (view != null) {
            view.showMemerLevelView(level);
            loadPrivilegesSize(level);
        }
    }

    private void loadMemberLevel() {
        mAccountHelper.loadMemberLevel(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo info) {
                LogUtil.d(TAG, "loadMemberLevel success");
                MemberLevel level = info.getMemberLevel();
                if (level == null) {
                    level = MemberLevel.GOLD;
                }
                IMemberCardContract.View view = mViewWeakReference.get();
                if (view != null) {
                    view.showMemerLevelView(level);
                    loadPrivilegesSize(level);
                }
            }

            @Override
            public void onFail(String errMessage) {

            }
        });
    }
}
