package com.gionee.gnservice.module.main;

import com.gionee.gnservice.entity.ServiceInfo;

public interface IModule {
    void startModule();

    ServiceInfo moduleInfo();

    boolean isModuleExist();
}
