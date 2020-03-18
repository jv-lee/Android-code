package com.lee.component.order.impl;

import android.support.v4.app.Fragment;

import com.lee.component.annotation.ARouter;
import com.lee.component.order.OrderFragment;
import com.lee.library.order.OrderFragmentCall;

/**
 * @author jv.lee
 * @date 2020/3/18
 * @description
 */
@ARouter(path = "/order/OrderFragment")
public class OrderFragmentImpl implements OrderFragmentCall {
    @Override
    public Fragment getFragment() {
        return new OrderFragment();
    }
}
