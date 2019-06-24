package com.examples.designpatterns.imple;

import android.util.Log;

import com.examples.designpatterns.api.Api;
import com.examples.designpatterns.UserInfo;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public class ApiImpl implements Api {
    @Override
    public UserInfo create() {
        UserInfo userInfo = new UserInfo();
        Log.i("lee >>>", userInfo.toString());
        return userInfo;
    }
}
