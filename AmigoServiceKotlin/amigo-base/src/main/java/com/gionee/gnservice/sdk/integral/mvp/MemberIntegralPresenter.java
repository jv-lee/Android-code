package com.gionee.gnservice.sdk.integral.mvp;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.account.IAccountHelper;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.domain.IntegralCase;
import com.gionee.gnservice.domain.Observer;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.IntegralInterface;
import com.gionee.gnservice.entity.IntegralTask;
import com.gionee.gnservice.sdk.integral.MemberIntegralActivity;
import com.gionee.gnservice.utils.LogUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by caocong on 5/4/17.
 */

public class MemberIntegralPresenter implements IMemberIntegralContract.Presenter {
    private static final String TAG = MemberIntegralActivity.class.getSimpleName();
    private IAppContext mAppContext;
    private WeakReference<IMemberIntegralContract.View> mView;
    private IntegralCase mIntegralCase;

    public MemberIntegralPresenter(IAppContext appContext, IMemberIntegralContract.View view) {
        mAppContext = appContext;
        mView = new WeakReference<IMemberIntegralContract.View>(view);
        mIntegralCase = new IntegralCase(appContext);
    }

    @Override
    public void loadIntegralPrizes() {
        mIntegralCase.getIntegralAllPrize((new Observer<List<String>>() {
            @Override
            public void onCompleted() {
                LogUtil.i(TAG, " load integral prize complete");
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.e(TAG, throwable.getMessage());
            }

            @Override
            public void onNext(List<String> s) {
                IMemberIntegralContract.View view = mView.get();
                if (view != null) {
                    view.showIntegralPrizeView(s);
                }
            }
        }));
    }

    @Override
    public void loadIntegralUser(String token) {
        mAppContext.accountHelper().loadUserInfo(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                if (accountInfo == null) {
                    LogUtil.i(TAG, "load integral user success, but account info is null");
                    return;
                }
                mIntegralCase.getIntegralUser(accountInfo.getToken(), mIntegralUserObserver);
            }

            @Override
            public void onFail(String errMessage) {
                LogUtil.e(TAG, "loadIntegralUser " + errMessage);
            }
        });
    }

    private Observer<IntegralInterface> mIntegralUserObserver = new Observer<IntegralInterface>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            LogUtil.e(TAG, throwable.getMessage());
        }

        @Override
        public void onNext(IntegralInterface value) {
            IMemberIntegralContract.View view = mView.get();
            if (view != null && value != null) {
                view.showIntegralUserView(value.getIntegralValue());
            }
        }
    };

    @Override
    public void loadLotteryRemainTimes(String token) {
        mAppContext.accountHelper().loadUserInfo(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                if (accountInfo == null) {
                    LogUtil.i(TAG, "load lottery remain times success, but account info is null");
                    return;
                }
                mIntegralCase.getLotteryTimes(accountInfo.getToken(), mLotteryTimesObserver);
            }

            @Override
            public void onFail(String errMessage) {
                LogUtil.e(TAG, "loadLotteryRemainTimes " + errMessage);
            }
        });
    }

    private Observer<Integer> mLotteryTimesObserver = new Observer<Integer>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            LogUtil.e(TAG, "load lottery times error");
        }

        @Override
        public void onNext(Integer times) {
            IMemberIntegralContract.View view = mView.get();
            if (view != null) {
                view.showLotteryRemainTimeView(times);
            }
        }
    };

    @Override
    public void loadIntegralMakeTasksNumber(String token) {
        mAppContext.accountHelper().loadUserInfo(AppConfig.Account.getAmigoServiceAppId(), new IAccountHelper.OnGetAccountInfoListener() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                if (accountInfo == null) {
                    LogUtil.i(TAG, "load integral make task number success, but account info is null");
                    return;
                }
                mIntegralCase.getIntegralTasks(accountInfo.getToken(), mIntegralMakeTasksNumberObserver);
            }

            @Override
            public void onFail(String errMessage) {
                LogUtil.e(TAG, "loadIntegralMakeTasksNumber " + errMessage);
            }
        });

    }

    private Observer<List<IntegralTask>> mIntegralMakeTasksNumberObserver = new Observer<List<IntegralTask>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {
            LogUtil.e(TAG, "get integral error");
        }

        @Override
        public void onNext(List<IntegralTask> integralTasks) {
            IMemberIntegralContract.View view = mView.get();
            if (view != null) {
                int num = 0;
                int size = 0;
                for (IntegralTask task : integralTasks) {
                    if (task.getStatus() == IntegralTask.STATUS_NOT_DONE) {
                        num += task.getValue();
                        size++;
                    }
                }
                view.showIntegralMakeTasksNumber(size, num);
            }
        }
    };

}
