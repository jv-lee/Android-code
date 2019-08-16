package com.gionee.gnservice.sdk.coupon;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.domain.CouponCase;
import com.gionee.gnservice.domain.Observer;
import com.gionee.gnservice.entity.CouponInfo;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by caocong on 5/27/17.
 */
class MemberCouponExpirePresenter implements IMemberCouponContract.Presenter {
    private static final String TAG = MemberCouponExpirePresenter.class.getSimpleName();
    private IAppContext mAppContext;
    private WeakReference<IMemberCouponContract.View> mView;
    private CouponCase mCouponCase;

    MemberCouponExpirePresenter(IAppContext appContext, IMemberCouponContract.View view) {
        mAppContext = appContext;
        mView = new WeakReference<IMemberCouponContract.View>(view);
    }

    @Override
    public void loadCoupons(final int pageNumber, final int pageSize) {
        load(pageNumber, pageSize);
    }

    private void load(int pageNumber, int pageSize) {
        IMemberCouponContract.View view = mView.get();
        if (view != null) {
            view.showLoadStartView();
        }
        if (mCouponCase == null) {
            mCouponCase = new CouponCase(mAppContext);
        }
        mCouponCase.getCouponsExpired(pageNumber, pageSize, new Observer<List<CouponInfo>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onNext(List<CouponInfo> couponInfos) {
                IMemberCouponContract.View view = mView.get();
                if (view != null) {
                    view.showCouponView(couponInfos);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                IMemberCouponContract.View view = mView.get();
                if (view != null) {
                    view.showLoadFailView();
                }
            }
        });
    }
}
