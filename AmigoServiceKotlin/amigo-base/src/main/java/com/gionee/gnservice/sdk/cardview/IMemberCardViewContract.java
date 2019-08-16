package com.gionee.gnservice.sdk.cardview;

import android.graphics.Bitmap;

import com.gionee.gnservice.base.IPresenter;
import com.gionee.gnservice.base.IView;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.entity.MemberPrivilege;

import java.util.List;

public interface IMemberCardViewContract {
    interface View extends IView {

        void showUnLoginView(MemberLevel level);

        //显示延保信息
        void showWarrantyInfo(String warranty);

        //显示会员特权
        void showPrivilegeView(List<MemberPrivilege> memberPrivilegeList);

        //显示会员优惠券多少张
        void showCouponNumView(String value);

        //显示A余额
        void showAcoinView(String acoin);

        //显示我的积分
        void showIntegralUser(String value);

        void showNetErrorView();

        void showMemberCardImg(List<Bitmap> bitmaps);

        void showUserInfo(AccountInfo info);

        void showPhoto(AccountInfo info);

        void showMemberLevel(MemberLevel level);

        void showNickName(AccountInfo info);
    }

    interface Presenter extends IPresenter {
        //加载帐号信息
        void loadAccountInfo();
    }

}
