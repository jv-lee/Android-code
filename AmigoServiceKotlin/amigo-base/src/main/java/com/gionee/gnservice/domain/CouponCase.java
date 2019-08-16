package com.gionee.gnservice.domain;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.domain.model.CouponModel;
import com.gionee.gnservice.entity.CouponInfo;

import java.util.List;

/**
 * Created by caocong on 5/27/17.
 */
public class CouponCase extends Case {
    private CouponModel mCouponModel;

    public CouponCase(IAppContext appContext) {
        super(appContext);
        mCouponModel = new CouponModel(appContext);
    }

    public void getCouponsUseable(int pageNumber, int pageSize, Observer<List<CouponInfo>> observer) {
        execute(mCouponModel.getCouponsUseable(pageNumber, pageSize), observer);

    }

    public void getCouponsExpired(int pageNumber, int pageSize, Observer<List<CouponInfo>> observer) {
        execute(mCouponModel.getCouponsExpired(pageNumber, pageSize), observer);
    }

    public void getCouponsUseableSize(String userId, Observer<Integer> observer) {
        execute(mCouponModel.getCouponsUseableSize(userId), observer);
    }
}
