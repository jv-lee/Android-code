package com.lee.component.order.impl;

import com.lee.component.annotation.ARouter;
import com.lee.component.order.R;
import com.lee.library.order.OrderDrawable;

/**
 * @author jv.lee
 * @date 2019-07-24
 * @description
 */
@ARouter(path = "/order/getDrawable")
public class OrderDrawableImpl implements OrderDrawable {
    @Override
    public int getDrawable() {
        return R.drawable.ic_all_out_black_24dp;
    }
}
