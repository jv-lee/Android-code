package com.lee.library.order;

import android.support.v4.app.Fragment;

import com.lee.component.api.core.Call;

/**
 * @author jv.lee
 * @date 2020/3/18
 * @description
 */
public interface OrderFragmentCall extends Call {
    /**
     * 供其他模块调用获取fragment实例
     *
     * @return
     */
    Fragment getFragment();
}
