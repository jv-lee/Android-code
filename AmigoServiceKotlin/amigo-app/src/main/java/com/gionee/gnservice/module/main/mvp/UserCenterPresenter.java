package com.gionee.gnservice.module.main.mvp;

import android.support.annotation.NonNull;
import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.domain.PrivilegeActionCase;
import com.gionee.gnservice.domain.ServiceInfoCase;
import com.gionee.gnservice.entity.PrivilegeAction;
import com.gionee.gnservice.entity.ServiceInfo;
import com.gionee.gnservice.utils.LogUtil;
import rx.Observer;

import java.lang.ref.WeakReference;
import java.util.List;

public class UserCenterPresenter implements IUserCenterContract.Presenter {
    private static final String TAG = UserCenterPresenter.class.getSimpleName();
    private IAppContext mAppContext;
    private WeakReference<IUserCenterContract.View> mViewWeakReference;
    private ServiceInfoCase mServiceInfoCase;
    private PrivilegeActionCase mPrivilegeActionCase;

    public UserCenterPresenter(@NonNull IAppContext appContext, @NonNull IUserCenterContract.View view) {
        this.mAppContext = appContext;
        this.mViewWeakReference = new WeakReference<>(view);
    }

    @Override
    public void loadPrivilegesAdds() {
        if (mPrivilegeActionCase == null) {
            mPrivilegeActionCase = new PrivilegeActionCase(mAppContext);
        }

        mPrivilegeActionCase.getPrivilegeActions(new com.gionee.gnservice.domain.Observer<List<PrivilegeAction>>() {
            @Override
            public void onCompleted() {
                LogUtil.i(TAG, "get privilege action complete");
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.e(TAG, throwable.getMessage());
            }

            @Override
            public void onNext(List<PrivilegeAction> privilegeActions) {
                IUserCenterContract.View view = mViewWeakReference.get();
                if (view != null) {
                    view.showPrivilegeAddsView(privilegeActions);
                }
            }
        });
    }

    @Override
    public void loadServiceInfo() {
        if (mServiceInfoCase == null) {
            mServiceInfoCase = new ServiceInfoCase(mAppContext);
        }
        mServiceInfoCase.getServiceInfos(new Observer<List<ServiceInfo>>() {
            @Override
            public void onCompleted() {
                LogUtil.i(TAG, "get service info complete");
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.e(TAG, throwable.getMessage());
            }

            @Override
            public void onNext(List<ServiceInfo> serviceInfos) {
                IUserCenterContract.View view = mViewWeakReference.get();
                if (view != null) {
                    view.showServiceView(serviceInfos);
                }
            }
        });
    }

}
