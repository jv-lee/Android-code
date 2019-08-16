package com.gionee.gnservice.domain;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.domain.data.ServiceInfoModel;
import com.gionee.gnservice.entity.ServiceInfo;
import rx.Observer;

import java.util.List;

/**
 * Created by caocong on 5/25/17.
 */
public class ServiceInfoCase extends Case2 {
    private ServiceInfoModel mServiceInfoModel;

    public ServiceInfoCase(IAppContext appContext) {
        super(appContext);
        mServiceInfoModel = new ServiceInfoModel(appContext);
    }

    public void getServiceInfos(Observer<List<ServiceInfo>> observer) {
        execute(mServiceInfoModel.getServiceInfos(), observer);
    }
}
