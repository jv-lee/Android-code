package com.lee.library.order;

import com.lee.component.api.core.Call;

/**
 * @author jv.lee
 * @date 2019-07-24
 * @description
 */
public interface OrderDrawable extends Call {
    /**
     * 供用其他模块调用Order的资源文件
     * @return
     */
    int getDrawable();
}
