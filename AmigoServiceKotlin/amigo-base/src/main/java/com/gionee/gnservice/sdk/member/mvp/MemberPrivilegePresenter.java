package com.gionee.gnservice.sdk.member.mvp;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.account.IAccountHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.domain.MemberPrivilegeCase;
import com.gionee.gnservice.domain.Observer;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.entity.MemberPrivilege;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by caocong on 1/10/17.
 */
public class MemberPrivilegePresenter implements IMemberPrivilegeContract.Presenter {
    private static final String TAG = MemberPrivilegePresenter.class.getSimpleName();
    private IAppContext mAppContext;
    private WeakReference<IMemberPrivilegeContract.View> mWeakReference;
    private MemberPrivilegeCase mMemberPrivilegeCase;

    public MemberPrivilegePresenter(IAppContext appContext, IMemberPrivilegeContract.View view) {
        PreconditionsUtil.checkNotNull(appContext);
        PreconditionsUtil.checkNotNull(view);
        mAppContext = appContext;
        mWeakReference = new WeakReference<IMemberPrivilegeContract.View>(view);
        mMemberPrivilegeCase = new MemberPrivilegeCase(appContext);
    }


    @Override
    public void loadMemberPrivileges(MemberLevel memberLevel) {
        mMemberPrivilegeCase.getMemberPrivileges(memberLevel, new Observer<List<MemberPrivilege>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.d(TAG, "loadMemberPrivileges onError");
                IMemberPrivilegeContract.View view = mWeakReference.get();
                if (view != null) {
                    view.showMemberPrivilegesLoadFailView(throwable);
                }
            }

            @Override
            public void onNext(List<MemberPrivilege> memberPrivileges) {
                IMemberPrivilegeContract.View view = mWeakReference.get();
                if (view != null) {
                    view.showMemberPrivileges(memberPrivileges);
                }
            }
        });
    }

    @Override
    public void loadAccountInfo() {
        mAppContext.accountHelper().getAccountInfo(IAccountHelper.GET_LEVEL | IAccountHelper.GET_NAME | IAccountHelper.GET_PHOTO,
                AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
                    @Override
                    public void onSuccess(AccountInfo accountInfo) {
                        LogUtil.d(TAG, "account info is:" + accountInfo);
                        IMemberPrivilegeContract.View view = mWeakReference.get();
                        if (view != null) {
                            view.showAccountView(accountInfo);
                        }
                    }

                    @Override
                    public void onFail(String errMessage) {
                        LogUtil.i(TAG, "onFail() errMessage = " + errMessage);
                        IMemberPrivilegeContract.View view = mWeakReference.get();
                        if (view != null) {
                            view.showAccountView(null);
                        }
                    }
                });
    }
}

