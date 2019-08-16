package com.gionee.gnservice.sdk.coupon;

import com.gionee.gnservice.base.IPresenter;
import com.gionee.gnservice.base.IView;
import com.gionee.gnservice.entity.CouponInfo;

import java.util.List;

/**
 * Created by caocong on 1/10/17.
 */
public interface IMemberCouponContract {

    interface View extends IView {
        void showLoadStartView();

        void showCouponView(List<CouponInfo> memberPrivilegeList);

        void showLoadFailView();
    }

    interface Presenter extends IPresenter {
        void loadCoupons(int pageNumber, int pageSize);
    }
}
