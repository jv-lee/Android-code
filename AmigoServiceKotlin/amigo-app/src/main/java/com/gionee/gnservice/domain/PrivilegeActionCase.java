package com.gionee.gnservice.domain;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.domain.data.PrivilegeActionModel;
import com.gionee.gnservice.entity.PrivilegeAction;

import java.util.List;

/**
 * Created by caocong on 5/31/17.
 */
public class PrivilegeActionCase extends Case {
    private PrivilegeActionModel mActionModel;

    public PrivilegeActionCase(IAppContext appContext) {
        super(appContext);
    }

    public void getPrivilegeActions(Observer<List<PrivilegeAction>> observer) {
        if (mActionModel == null) {
            mActionModel = new PrivilegeActionModel(mAppContext);
        }
        execute(mActionModel.loadPrivilegeActions(), observer);
    }

}

