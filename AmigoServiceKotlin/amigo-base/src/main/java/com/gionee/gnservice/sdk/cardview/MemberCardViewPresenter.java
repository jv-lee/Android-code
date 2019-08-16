package com.gionee.gnservice.sdk.cardview;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.account.AccountHelperImpl;
import com.gionee.gnservice.common.account.IAccountHelper;
import com.gionee.gnservice.common.cache.TCache;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.domain.AcoinCase;
import com.gionee.gnservice.domain.CouponCase;
import com.gionee.gnservice.domain.IntegralCase;
import com.gionee.gnservice.domain.MemberCardImgCase;
import com.gionee.gnservice.domain.MemberPrivilegeCase;
import com.gionee.gnservice.domain.Observer;
import com.gionee.gnservice.domain.WarrantyCase;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.IntegralInterface;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.entity.MemberPrivilege;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

public class MemberCardViewPresenter implements IMemberCardViewContract.Presenter {
    private static final String TAG = MemberCardViewPresenter.class.getSimpleName();
    private IAppContext mAppContext;
    private WeakReference<IMemberCardViewContract.View> mViewWeakReference;
    private TCache mTCache;
    private MemberPrivilegeCase mMemberPrivilegeCase;
    private AcoinCase mAcoinCase;
    private WarrantyCase mWarrantyCase;
    private IntegralCase mIntegralCase;
    private CouponCase mCouponCase;
    private MemberCardImgCase mMemberCardImgCase;
    private IAccountHelper mAccountHelper;

    private static class CacheKey {
        private static final String KEY_ACCOUNT_INFO = "card/accountinfo";
        private static final String KEY_PHOTO = "card/photo";
        private static final String KEY_MEMBER_LEVEL = "card/memberlevel";
        private static final String KEY_ACOIN = "card/coinbalance";
        private static final String KEY_COUPON_INFO = "card/couponinfo";
        private static final String KEY_WARRANTY_INFO = "card/warrantyinfo";
        private static final String KEY_INTEGRAL_USER = "card/integraluser"; //后续版本测试没问题后可以删除
        private static final String KEY_PRIVILEGE_INFO = "card/privilegeinfo";
        private static final String KEY_NICKNAME = "card/nickname";
        //替换之前版本中的整数型积分缓存
        private static final String KEY_INTEGRAL_INFO = "card/integralinfo";
    }

    public MemberCardViewPresenter(IAppContext appContext, IMemberCardViewContract.View view) {
        PreconditionsUtil.checkNotNull(appContext);
        PreconditionsUtil.checkNotNull(view);
        this.mAppContext = appContext;
        this.mViewWeakReference = new WeakReference<IMemberCardViewContract.View>(view);
        mTCache = TCache.get(mAppContext.application());
        mAccountHelper = mAppContext.accountHelper();
    }

    @Override
    public void loadAccountInfo() {
        LogUtil.i(TAG, "begin to load account info");
        mLoginTask.execute();
    }

    private AsyncTask<Object, Object, Boolean> mLoginTask = new AsyncTask<Object, Object, Boolean>() {
        @Override
        protected Boolean doInBackground(Object... params) {
            //sometimes this value seems be confusion, so print it.
            boolean isLogin = mAccountHelper.isLogin();
            LogUtil.i(TAG, "account login status: " + isLogin);
            return isLogin;
        }

        @Override
        protected void onPostExecute(Boolean isLogin) {
            if (isLogin) {
                loadUserInfo();
                loadPhoto();
                //loadMemberLevel();
            } else {
                LogUtil.i(TAG, "show unlogin card!!!");
                MemberLevel level = getCacheMemberLevel();
                loadMemberCardImage(level);
                IMemberCardViewContract.View view = mViewWeakReference.get();
                if (view != null) {
                    view.showUnLoginView(level);
                }
            }
        }
    };

    private MemberLevel getCacheMemberLevel() {
        if (mTCache.isCached(CacheKey.KEY_INTEGRAL_INFO)) {
            IntegralInterface info = mTCache.getSerializable(CacheKey.KEY_INTEGRAL_INFO);
            if (info == null) {
                return MemberLevel.GOLD;
            } else {
                MemberLevel level = AccountHelperImpl.parseMemberLevel(info.getRankValue());
                return level == null ? MemberLevel.GOLD : level;
            }
        }

        return MemberLevel.GOLD;
    }

    private void loadUserInfo() {
        loadUserInfoFromCache();
        loadIntegralFromCache();
        loadAcoinFromCache();
        loadCouponInfoFromCache();
        loadWarrantyInfoFromCache();
        loadNickNameCache();
        loadPrivilegeInfoFromCache();

        mAccountHelper.loadUserInfo(AppConfig.Account.getAmigoServiceAppId(),
                new IAccountHelper.OnGetAccountInfoListener() {
                    @Override
                    public void onSuccess(AccountInfo info) {
                        LogUtil.i(TAG, "loadUserInfo success");
                        if (info != null) {
                            mTCache.putSerializable(CacheKey.KEY_ACCOUNT_INFO, info);
                        }

                        IMemberCardViewContract.View view = mViewWeakReference.get();
                        if (view != null && info != null) {
                            view.showUserInfo(info);
                            loadIntegralInfo(info.getToken());
                            loadAcoin(info.getUserId());
                            loadCouponInfo(info.getUserId());
                            loadWarrantyInfo(info.getToken());
                            loadNickName();
                        }
                    }

                    @Override
                    public void onFail(String errMessage) {
                        LogUtil.e(TAG, "loadUserInfo failed, msg:" + errMessage);
                        // TODO: 17-9-21 增加容错处理，否则卡片可能显示异常
                        //recoveryErrorState();
                    }
                });
    }

    /****************************从缓存加载用户信息****************************/
    private void loadUserInfoFromCache() {
        if (mTCache.isCached(CacheKey.KEY_ACCOUNT_INFO)) {
            AccountInfo accountInfo = mTCache.getSerializable(CacheKey.KEY_ACCOUNT_INFO);
            IMemberCardViewContract.View view = mViewWeakReference.get();
            if (view != null && accountInfo != null) {
                LogUtil.i(TAG, "load user info from cache");
                view.showUserInfo(accountInfo);
            }
        }
    }

    /****************************加载积分、等级、成长值信息********************************/
    private void loadIntegralFromCache() {
        if (mTCache.isCached(CacheKey.KEY_INTEGRAL_INFO)) {
            IntegralInterface integralInfo = mTCache.getSerializable(CacheKey.KEY_INTEGRAL_INFO);
            IMemberCardViewContract.View view = mViewWeakReference.get();
            if (view != null && integralInfo != null) {
                LogUtil.i(TAG, "load integral fromo cache");
                view.showIntegralUser(String.valueOf(integralInfo.getIntegralValue()));
                MemberLevel memberLevel = AccountHelperImpl.parseMemberLevel(integralInfo.getRankValue());
                view.showMemberLevel(memberLevel);
                loadMemberCardImage(memberLevel);
            }
        }
    }

    private void recoveryErrorState() {
        LogUtil.i(TAG, "recovery card display");
        IMemberCardViewContract.View view = mViewWeakReference.get();
        if (view == null) {
            LogUtil.e(TAG, "view was recycled, can not recovery!");
            return;
        }

        if (mTCache.isCached(CacheKey.KEY_INTEGRAL_INFO)) {
            IntegralInterface integralInfo = mTCache.getSerializable(CacheKey.KEY_INTEGRAL_INFO);
            if (integralInfo != null) {
                LogUtil.i(TAG, "use cache data to recovery card display.");
                view.showIntegralUser(String.valueOf(integralInfo.getIntegralValue()));
                MemberLevel memberLevel = AccountHelperImpl.parseMemberLevel(integralInfo.getRankValue());
                view.showMemberLevel(memberLevel);
                loadMemberCardImage(memberLevel);
                return;
            }
        }

        LogUtil.i(TAG, "no cache or cache data is null, use default value to display.");
        view.showIntegralUser(String.valueOf(0));
        view.showMemberLevel(MemberLevel.GOLD);
        loadMemberCardImage(MemberLevel.GOLD);
    }

    private void loadIntegralInfo(String token) {
        if (mIntegralCase == null) {
            mIntegralCase = new IntegralCase(mAppContext);
        }
        mIntegralCase.getIntegralUser(token, mIntegralUserObserver);
    }

    private Observer<IntegralInterface> mIntegralUserObserver = new Observer<IntegralInterface>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            LogUtil.e(TAG, "load integral error");
        }

        @Override
        public void onNext(IntegralInterface integralInterface) {
            if (integralInterface != null) {
                LogUtil.i(TAG, "loaded integral success: " + integralInterface.toString());
                mTCache.putSerializable(CacheKey.KEY_INTEGRAL_INFO, integralInterface);
            }

            IMemberCardViewContract.View view = mViewWeakReference.get();
            if (view != null && integralInterface != null) {
                view.showIntegralUser(String.valueOf(integralInterface.getIntegralValue()));
                MemberLevel memberLevel = AccountHelperImpl.parseMemberLevel(integralInterface.getRankValue());
                memberLevel.setGrowthValue(integralInterface.getGrowthValue());
                view.showMemberLevel(memberLevel);
                loadMemberCardImage(memberLevel);
                loadPrivilegeInfo(memberLevel);
            }
        }
    };

    /***************************加载A币信息******************************/
    private void loadAcoinFromCache() {
        if (mTCache.isCached(CacheKey.KEY_ACOIN)) {
            String coin = mTCache.getSerializable(CacheKey.KEY_ACOIN);
            IMemberCardViewContract.View view = mViewWeakReference.get();
            if (view != null && coin != null) {
                LogUtil.i(TAG, "load ACoin from cache");
                view.showAcoinView(String.valueOf(coin));
            }
        }
    }

    private void loadAcoin(String userId) {
        if (mAcoinCase == null) {
            mAcoinCase = new AcoinCase(mAppContext);
        }
        mAcoinCase.getACoinBalance(userId, mAcoinObserver);
    }

    private Observer<String> mAcoinObserver = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            LogUtil.e(TAG, "load ACoin error");
        }

        @Override
        public void onNext(String coin) {
            if (coin != null) {
                LogUtil.i(TAG, "loaded ACoin value is:" + coin);
                mTCache.putSerializable(CacheKey.KEY_ACOIN, coin);
            }

            IMemberCardViewContract.View view = mViewWeakReference.get();
            if (view != null && coin != null) {
                view.showAcoinView(coin);
            }
        }
    };

    /****************************加载优惠券信息**********************************/
    private void loadCouponInfoFromCache() {
        if (mTCache.isCached(CacheKey.KEY_COUPON_INFO)) {
            Integer couponInfo = mTCache.getSerializable(CacheKey.KEY_COUPON_INFO);
            IMemberCardViewContract.View view = mViewWeakReference.get();
            if (view != null && couponInfo != null) {
                LogUtil.i(TAG, "load coupon info from cache");
                view.showCouponNumView(String.valueOf(couponInfo));
            }
        }
    }

    private void loadCouponInfo(String userId) {
        if (mCouponCase == null) {
            mCouponCase = new CouponCase(mAppContext);
        }
        mCouponCase.getCouponsUseableSize(userId, mCouponObserver);
    }

    private Observer<Integer> mCouponObserver = new Observer<Integer>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            LogUtil.e(TAG, "load coupon info error");
        }

        @Override
        public void onNext(Integer integer) {
            if (integer != null) {
                LogUtil.i(TAG, "loaded coupon value is: " + integer);
                mTCache.putSerializable(CacheKey.KEY_COUPON_INFO, integer);
            }

            IMemberCardViewContract.View view = mViewWeakReference.get();
            if (view != null && integer != null) {
                view.showCouponNumView(String.valueOf(integer));
            }
        }
    };

    /************************加载电子保卡信息**************************/
    private void loadWarrantyInfoFromCache() {
        if (mTCache.isCached(CacheKey.KEY_WARRANTY_INFO)) {
            Integer warrantyInfo = mTCache.getSerializable(CacheKey.KEY_WARRANTY_INFO);
            IMemberCardViewContract.View view = mViewWeakReference.get();
            if (view != null && warrantyInfo != null) {
                LogUtil.i(TAG, "load warranty from cache");
                view.showWarrantyInfo(String.valueOf(warrantyInfo));
            }
        }
    }

    private void loadWarrantyInfo(String token) {
        if (mWarrantyCase == null) {
            mWarrantyCase = new WarrantyCase(mAppContext);
        }
        mWarrantyCase.getWarranty(token, mWarrantyInfoObserver);
    }

    private Observer<Integer> mWarrantyInfoObserver = new Observer<Integer>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            LogUtil.e(TAG, "load warranty info error");
        }

        @Override
        public void onNext(Integer integer) {
            if (integer != null) {
                LogUtil.i(TAG, "loaded warranty value is: " + integer);
                mTCache.putSerializable(CacheKey.KEY_WARRANTY_INFO, integer);
            }

            IMemberCardViewContract.View view = mViewWeakReference.get();
            if (view != null) {
                view.showWarrantyInfo(String.valueOf(integer));
            }
        }
    };

    /************************加载用户昵称************************************/
    private void loadNickNameCache() {
        if (mTCache.isCached(CacheKey.KEY_NICKNAME)) {
            AccountInfo info = mTCache.getSerializable(CacheKey.KEY_NICKNAME);
            IMemberCardViewContract.View view = mViewWeakReference.get();
            if (view != null && info != null) {
                LogUtil.i(TAG, "load nick name from cache");
                view.showNickName(info);
            }
        }
    }

    private void loadNickName() {
        mAccountHelper.loadNickName(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo info) {
                LogUtil.i(TAG, "load nick name success");
                if (info != null) {
                    mTCache.putSerializable(CacheKey.KEY_NICKNAME, info);
                }

                IMemberCardViewContract.View view = mViewWeakReference.get();
                if (view != null && info != null) {
                    view.showNickName(info);
                }
            }

            @Override
            public void onFail(String errMessage) {
                LogUtil.e(TAG, "load nick name fail, msg: " + errMessage);
            }
        });
    }

    /************************加载会员特权信息****************************/
    private void loadPrivilegeInfoFromCache() {
        if (mTCache.isCached(CacheKey.KEY_PRIVILEGE_INFO)) {
            List<MemberPrivilege> memberPrivileges = mTCache.getSerializable(CacheKey.KEY_PRIVILEGE_INFO);
            IMemberCardViewContract.View view = mViewWeakReference.get();
            if (view != null && memberPrivileges != null) {
                LogUtil.i(TAG, "load privilege info from cache");
                view.showPrivilegeView(memberPrivileges);
            }
        }
    }

    private void loadPrivilegeInfo(final MemberLevel memberLevel) {
        if (mMemberPrivilegeCase == null) {
            mMemberPrivilegeCase = new MemberPrivilegeCase(mAppContext);
        }
        mMemberPrivilegeCase.getMemberPrivileges(memberLevel, mMemberPrivilegeObserver);
    }

    Observer<List<MemberPrivilege>> mMemberPrivilegeObserver =
            new Observer<List<MemberPrivilege>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onNext(List<MemberPrivilege> memberPrivileges) {
                    LogUtil.i(TAG, "load memberprivilegs success");
                    IMemberCardViewContract.View view = mViewWeakReference.get();
                    if (memberPrivileges != null) {
                        mTCache.putSerializable(CacheKey.KEY_PRIVILEGE_INFO, (Serializable) memberPrivileges);
                    }
                    if (view != null && memberPrivileges != null) {
                        view.showPrivilegeView(memberPrivileges);
                    }
                }
            };

    /****************************加载会员头像***********************************/
    private void loadPhotoFromCache() {
        if (mTCache.isCached(CacheKey.KEY_PHOTO)) {
            AccountInfo cacheInfo = mTCache.getSerializable(CacheKey.KEY_PHOTO);
            IMemberCardViewContract.View view = mViewWeakReference.get();
            LogUtil.d(TAG, "load photo from cache");
            if (view != null && cacheInfo != null) {
                view.showPhoto(cacheInfo);
            }
        }
    }

    private void loadPhoto() {
        loadPhotoFromCache();

        mAccountHelper.loadPhoto(AppConfig.Account.getAmigoServiceAppId(),
                new IAccountHelper.OnGetAccountInfoListener() {
                    @Override
                    public void onSuccess(AccountInfo info) {
                        LogUtil.i(TAG, "load photo success");
                        IMemberCardViewContract.View view = mViewWeakReference.get();
                        if (info != null) {
                            mTCache.putSerializable(CacheKey.KEY_PHOTO, info);
                        }
                        if (view != null && info != null) {
                            view.showPhoto(info);
                        }
                    }

                    @Override
                    public void onFail(String errMessage) {
                        LogUtil.e(TAG, "load photo fail, msg: " + errMessage);
                    }
                });
    }

    /************************加载会员卡片信息*****************************/
    private void loadMemberCardImage(MemberLevel memberLevel) {
        if (mMemberCardImgCase == null) {
            mMemberCardImgCase = new MemberCardImgCase(mAppContext);
        }
        mMemberCardImgCase.getMemberCardImgs(memberLevel, mMemberCardImgObserver);
    }

    private Observer<List<Bitmap>> mMemberCardImgObserver = new Observer<List<Bitmap>>() {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            LogUtil.e(TAG, "load card img error");
        }

        @Override
        public void onNext(List<Bitmap> bitmaps) {
            IMemberCardViewContract.View view = mViewWeakReference.get();
            LogUtil.i(TAG, "loaded card img success");
            if (view != null && bitmaps != null) {
                view.showMemberCardImg(bitmaps);
            }
        }
    };

    /*private void loadMemberLevel() {
        loadMemberLevelFromCache();
        loadPrivilegeInfoFromCache();

        mAccountHelper.loadMemberLevel(AppConfig.Account.getAmigoServiceAppId(),
                new IAccountHelper.OnGetAccountInfoListener() {
                    @Override
                    public void onSuccess(AccountInfo info) {
                        LogUtil.d(TAG, "loadMemberLevel success");
                        IMemberCardViewContract.View view = mViewWeakReference.get();
                        MemberLevel level = null;
                        if (info != null) {
                            level = info.getMemberLevel();
                            mTCache.putSerializable(CacheKey.KEY_MEMBER_LEVEL, info);
                        }
                        if (view != null && level != null) {
                            view.showMemberLevel(level);
                            loadMemberCardImage(level);
                            loadPrivilegeInfo(level);
                        }
                    }

                    @Override
                    public void onFail(String errMessage) {
                        LogUtil.d(TAG, "loadMemberLevel onFail() errMessage = " + errMessage);
                    }
                }
        );
    }*/

    /*private void loadMemberLevelFromCache() {
        LogUtil.d(TAG, "loadMemberLevelFromCache()");
        if (mTCache.isCached(CacheKey.KEY_MEMBER_LEVEL)) {
            AccountInfo cacheInfo = mTCache.getSerializable(CacheKey.KEY_MEMBER_LEVEL);
            MemberLevel level = cacheInfo.getMemberLevel();
            if (level == null) {
                LogUtil.d(TAG, "cached level is null");
                level = MemberLevel.GOLD;
            }
            IMemberCardViewContract.View view = mViewWeakReference.get();
            if (view != null) {
                view.showMemberLevel(cacheInfo.getMemberLevel());
                loadMemberCardImage(level);
            }
        }
    }*/

}
