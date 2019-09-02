package com.lee.glide.load;

import com.lee.glide.resource.Value;

/**
 * @author jv.lee
 * @date 2019-09-02
 * @description 加载外部资源 成功/失败 回调
 */
public interface ResponseListener {

    /**
     * 资源加载回调成功
     * @param value
     */
    void responseSuccess(Value value);

    /**
     * 资源加载回调失败
     * @param e 错误信息
     */
    void responseError(Exception e);

}
