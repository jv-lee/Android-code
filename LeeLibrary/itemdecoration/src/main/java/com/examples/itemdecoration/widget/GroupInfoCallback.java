package com.examples.itemdecoration.widget;

import com.examples.itemdecoration.entity.GroupInfo;

/**
 * @author jv.lee
 * @date 2019/5/22.
 * description：
 */
public interface GroupInfoCallback {
    GroupInfo getGroupInfo(int position);
}
