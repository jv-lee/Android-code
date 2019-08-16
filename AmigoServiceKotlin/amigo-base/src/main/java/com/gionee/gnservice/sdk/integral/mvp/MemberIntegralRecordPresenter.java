package com.gionee.gnservice.sdk.integral.mvp;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.account.IAccountHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.domain.IntegralCase;
import com.gionee.gnservice.domain.Observer;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.IntegralInterface;
import com.gionee.gnservice.entity.IntegralRecord;
import com.gionee.gnservice.utils.LogUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by caocong on 5/4/17.
 */

public class MemberIntegralRecordPresenter implements IMemberIntegralRecordContract.Presenter {

    private static final String TAG = MemberIntegralRecordPresenter.class.getSimpleName();
    private IAppContext mAppContext;
    private WeakReference<IMemberIntegralRecordContract.View> mView;
    private IntegralCase mIntegralCase;

    public MemberIntegralRecordPresenter(IAppContext appContext, IMemberIntegralRecordContract.View view) {
        mAppContext = appContext;
        mView = new WeakReference<IMemberIntegralRecordContract.View>(view);
        mIntegralCase = new IntegralCase(appContext);
    }

    @Override
    public void loadIntegralRecords(final int page) {
        LogUtil.d(TAG, "loadIntegralRecords,page is:" + page);
        mAppContext.accountHelper().loadUserInfo(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                String token = accountInfo.getToken();
                LogUtil.d(TAG, "loadIntegralRecords,get token is:" + token);
                loadIntegralRecords(page, token);
            }

            @Override
            public void onFail(String errMessage) {
                LogUtil.d(TAG, "get token fail " + errMessage);
                IMemberIntegralRecordContract.View view = mView.get();
                if (view != null) {
                    view.showLoadFailView();
                }
            }
        });

    }

    @Override
    public void loadObtainRecords(final int page) {
        LogUtil.d(TAG, "loadObtainRecords,page is:" + page);
        mAppContext.accountHelper().loadUserInfo(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                String token = accountInfo.getToken();
                LogUtil.d(TAG, "loadObtainRecords get token is:" + token);
                loadObtainRecords(page, token);
            }

            @Override
            public void onFail(String errMessage) {
                IMemberIntegralRecordContract.View view = mView.get();
                if (view != null) {
                    view.showLoadFailView();
                }
            }
        });

    }

    @Override
    public void loadIntegralUser() {
        mAppContext.accountHelper().loadUserInfo(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                String token = accountInfo.getToken();
                LogUtil.d(TAG, "loadIntegralUser get token is:" + token);
                mIntegralCase.getIntegralUser(token, mIntegralUserObserver);
            }

            @Override
            public void onFail(String errMessage) {
                IMemberIntegralRecordContract.View view = mView.get();
                if (view != null) {
                    view.showLoadFailView();
                }
            }
        });
    }

    private Observer<IntegralInterface> mIntegralUserObserver = new Observer<IntegralInterface>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onNext(IntegralInterface value) {
            IMemberIntegralRecordContract.View view = mView.get();
            if (view != null && value != null) {
                view.showIntegralUserView(value.getIntegralValue());
            }
        }
    };

    private void loadIntegralRecords(int page, String token) {
        mIntegralCase.getAllIntegralRecords(token, page, mIntegralRecordObserver);
    }

    private Observer<List<IntegralRecord>> mIntegralRecordObserver = new Observer<List<IntegralRecord>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            IMemberIntegralRecordContract.View view = mView.get();
            if (view != null) {
                view.showLoadFailView();
            }
        }

        @Override
        public void onNext(List<IntegralRecord> integralRecords) {
            IMemberIntegralRecordContract.View view = mView.get();
            if (view != null) {
                view.showIntegralRecordView(integralRecords);
            }
        }
    };

    private void loadObtainRecords(int page, String token) {
        mIntegralCase.getObtainIntegralRecords(token, page, mIntegralObtainRecordObserver);
    }

    private Observer<List<IntegralRecord>> mIntegralObtainRecordObserver = new Observer<List<IntegralRecord>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            IMemberIntegralRecordContract.View view = mView.get();
            if (view != null) {
                view.showLoadFailView();
            }
        }

        @Override
        public void onNext(List<IntegralRecord> integralRecords) {
            IMemberIntegralRecordContract.View view = mView.get();
            if (view != null) {
                view.showIntegralRecordView(integralRecords);
            }
        }
    };
}
