package com.gionee.gnservice.sdk.integral.mvp;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.account.IAccountHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.domain.IntegralCase;
import com.gionee.gnservice.domain.Observer;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.IntegralTask;
import com.gionee.gnservice.entity.IntegralTaskResult;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by caocong on 5/4/17.
 */

public class MemberIntegralMakePresenter implements IMemberIntegralMakeContract.Presenter {
    private static final String TAG = MemberIntegralMakePresenter.class.getSimpleName();
    private IAppContext mAppContext;
    private WeakReference<IMemberIntegralMakeContract.View> mView;
    private IntegralCase mIntegralCase;

    public MemberIntegralMakePresenter(IAppContext appContext, IMemberIntegralMakeContract.View view) {
        PreconditionsUtil.checkNotNull(appContext);
        mAppContext = appContext;
        mView = new WeakReference<IMemberIntegralMakeContract.View>(view);
        mIntegralCase = new IntegralCase(mAppContext);
    }


    @Override
    public void loadIntegralTasks() {

        LogUtil.d(TAG, "loadIntegralTask");
        mAppContext.accountHelper().loadUserInfo(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                getIntegralTasks(accountInfo.getToken());
            }

            @Override
            public void onFail(String errMessage) {
                IMemberIntegralMakeContract.View view = mView.get();
                if (view != null) {
                    view.showLoadFailView();
                }
            }
        });
    }

    @Override
    public void loadPrize() {
        mIntegralCase.getIntegralMakePrizes((new Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(List<String> s) {
                IMemberIntegralMakeContract.View view = mView.get();
                if (view != null) {
                    view.showPrizeView(s);
                }
            }
        }));
    }

    @Override
    public void uploadIntegralTask(final int id) {
        LogUtil.d(TAG, "uploadIntegralTask");
        mAppContext.accountHelper().loadUserInfo(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                uploadIntegralTask(accountInfo.getToken(), id);
            }

            @Override
            public void onFail(String errMessage) {
                IMemberIntegralMakeContract.View view = mView.get();
                if (view != null) {
                    view.showLoadFailView();
                }
            }
        });
    }

    private void uploadIntegralTask(String token, int id) {
        mIntegralCase.uploadIntegralTask(token, id, new Observer<IntegralTaskResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable var1) {

            }

            @Override
            public void onNext(IntegralTaskResult result) {
                IMemberIntegralMakeContract.View view = mView.get();
                if (view != null) {
                    view.showUploadIntegralTaskView(result);
                }
            }
        });
    }

    private void getIntegralTasks(String token) {
        mIntegralCase.getIntegralTasks(token, new Observer<List<IntegralTask>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                IMemberIntegralMakeContract.View view = mView.get();
                if (view != null) {
                    view.showLoadFailView();
                }
            }

            @Override
            public void onNext(List<IntegralTask> integralTasks) {
                IMemberIntegralMakeContract.View view = mView.get();
                if (view != null) {
                    LogUtil.d(TAG, "get integral task is:" + integralTasks + "view name is:" + view.getClass().getSimpleName());
                    view.showIntegralTaskView(integralTasks);
                }
            }
        });
    }

}