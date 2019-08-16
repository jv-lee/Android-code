package com.gionee.gnservice.domain.data;

import android.support.annotation.NonNull;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.domain.model.BaseModel;
import com.gionee.gnservice.entity.ServiceInfo;
import com.gionee.gnservice.module.main.BaseModule;
import com.gionee.gnservice.module.main.ModulesXmlHelper;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PhoneUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class ServiceInfoModel extends BaseModel {
    private static final String TAG = ServiceInfoModel.class.getSimpleName();

    public ServiceInfoModel(@NonNull IAppContext appContext) {
        super(appContext);
    }

    public Observable<List<ServiceInfo>> getServiceInfos() {
        return Observable
                .create(new Observable.OnSubscribe<List<ServiceInfo>>() {
                    @Override
                    public void call(Subscriber<? super List<ServiceInfo>> subscriber) {
                        ModulesXmlHelper xmlHelper = new ModulesXmlHelper(mAppContext.application());
                        PreconditionsUtil.checkNotNull(xmlHelper);
                        List<ServiceInfo> moduleInfoList = xmlHelper.getDefaultFromAsset("user_center_modules_config.xml");
                        moduleInfoList = filterModuleList(moduleInfoList);
                        subscriber.onNext(moduleInfoList);
                        subscriber.onCompleted();
                    }
                });
    }

    //过滤
    private List<ServiceInfo> filterModuleList(List<ServiceInfo> moduleInfoList) {
        List<ServiceInfo> infoList = checkShouldHideSynchronizer(moduleInfoList);
        // FIXME: 17-8-3 需求变更，对应180623需求
        /*List<ServiceInfo> infos = new ArrayList<ServiceInfo>();
        for (ServiceInfo info : infoList) {
            if (isModuleApkAndNotExist(info)) {
                continue;
            }
            infos.add(info);
        }
        return infos;*/
        return infoList;
    }

    private boolean isModuleApkAndNotExist(ServiceInfo moduleInfo) {
        BaseModule module = new BaseModule(mAppContext.application(), moduleInfo);
        return moduleInfo.isApk() && !module.isModuleExist();
    }

    /**
     * 移动定制机隐藏云服务入口，且用官方售后来代替云服务的位置
     */
    private List<ServiceInfo> checkShouldHideSynchronizer(List<ServiceInfo> moduleInfoList) {
        ServiceInfo infoSynchronize = null;
        boolean isMobileCustomizationVersion = false;
        for (ServiceInfo info : moduleInfoList) {
            if ("gn.com.android.synchronizer.WelcomeActivity".equals(info.getTarget())) {
                infoSynchronize = info;
                if (PhoneUtil.isMobileCustomizationVersion()) {
                    isMobileCustomizationVersion = true;
                }
            }
        }
        if (isMobileCustomizationVersion) {
            moduleInfoList.remove(infoSynchronize);
        }
        return moduleInfoList;
    }
}
