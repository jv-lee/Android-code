package com.gionee.gnservice.domain;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.domain.model.MemberPrivilegeModel;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.entity.MemberPrivilege;

import java.util.List;

/**
 * Created by caocong on 5/24/17.
 */
public class MemberPrivilegeCase extends Case {
    private MemberPrivilegeModel mMemberPrivilegeModel;

    public MemberPrivilegeCase(IAppContext appContext) {
        super(appContext);
        mMemberPrivilegeModel = new MemberPrivilegeModel(appContext);
    }

    public void getMemberPrivileges(MemberLevel level, Observer<List<MemberPrivilege>> observer) {
        execute(mMemberPrivilegeModel.getMemberPivileges(level), observer);
    }

    public void getMemberPrivilegesSize(MemberLevel level, Observer<Integer> observer) {
        execute(mMemberPrivilegeModel.getMemberPivilegesSize(level), observer);
    }
}
