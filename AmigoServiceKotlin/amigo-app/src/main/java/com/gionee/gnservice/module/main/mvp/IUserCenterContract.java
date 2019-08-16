package com.gionee.gnservice.module.main.mvp;

import com.gionee.gnservice.base.IPresenter;
import com.gionee.gnservice.base.IView;
import com.gionee.gnservice.entity.PrivilegeAction;
import com.gionee.gnservice.entity.ServiceInfo;

import java.util.List;

public interface IUserCenterContract {
    interface View extends IView {

        //显示会员服务
        void showServiceView(List<ServiceInfo> moduleInfoList);

        //显示会员特权广告
        void showPrivilegeAddsView(List<PrivilegeAction> privilegeActions);

    }

    interface Presenter extends IPresenter {

        //加载会员特权广告
        void loadPrivilegesAdds();

        //加载会员服务
        void loadServiceInfo();
    }
}
