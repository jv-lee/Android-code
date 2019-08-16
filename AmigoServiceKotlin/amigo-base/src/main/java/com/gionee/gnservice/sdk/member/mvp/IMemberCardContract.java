package com.gionee.gnservice.sdk.member.mvp;

import android.graphics.Bitmap;

import com.gionee.gnservice.base.IPresenter;
import com.gionee.gnservice.base.IView;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.MemberLevel;

public interface IMemberCardContract {

    interface View extends IView {
        void showAccountView(AccountInfo accountInfo);

        void showPhoto(Bitmap photo);

        void showMemerLevelView(MemberLevel level);

        void showACoinBalance(String coin);

        void showPrivilegesSize(int size);

        void showUserIntegral(int value);

        void showCouponNumber(int value);

    }

    interface Presenter extends IPresenter {

        void loadAccountInfo();
//        void loadACoinBalance(String userId);
//
//        void loadAccountInfo();
//
//        void loadPrivilegesSize(MemberLevel level);
//
//        void loadUserIntegral(String token);
//
//        void loadCouponNumber(String userId);

    }
}
