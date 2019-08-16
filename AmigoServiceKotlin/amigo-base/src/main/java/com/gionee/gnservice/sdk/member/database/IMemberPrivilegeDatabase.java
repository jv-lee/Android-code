package com.gionee.gnservice.sdk.member.database;

import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.entity.MemberPrivilege;

import java.util.List;

/**
 * Created by caocong on 1/10/17.
 */
public interface IMemberPrivilegeDatabase {
    boolean saveMemberPrivileges(List<MemberPrivilege> memberPrivileges);

    List<MemberPrivilege> queryMemberPrivilegesByMemberLevel(MemberLevel memberLevel);

    int queryVersion();

    boolean saveVersion(String version);

    long queryLastUpdateTime();

    boolean saveLastUpdateTime(long updateTime);

    boolean deleteAllDatas();
}
