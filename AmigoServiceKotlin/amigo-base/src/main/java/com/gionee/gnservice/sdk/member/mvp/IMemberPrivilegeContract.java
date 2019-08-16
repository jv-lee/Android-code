package com.gionee.gnservice.sdk.member.mvp;

import com.gionee.gnservice.base.IPresenter;
import com.gionee.gnservice.base.IView;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.entity.MemberPrivilege;

import java.util.List;

/**
 * Created by caocong on 1/10/17.
 */
public interface IMemberPrivilegeContract {

    interface View extends IView {
        void showMemberPrivilegesLoadingView();

        void showMemberPrivileges(List<MemberPrivilege> memberPrivilegeList);

        void showMemberPrivilegesLoadFailView(Throwable throwable);

        void showAccountView(AccountInfo accountInfo);
    }

    interface Presenter extends IPresenter {
        void loadMemberPrivileges(MemberLevel memberLevel);

        void loadAccountInfo();
    }
}
