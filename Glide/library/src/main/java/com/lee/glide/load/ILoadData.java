package com.lee.glide.load;

import android.content.Context;

import com.lee.glide.resource.Value;

/**
 * @author jv.lee
 * @date 2019-09-02
 * @description 加载外部资源标准
 */
public interface ILoadData {

    /**
     * 加载外部资源的行为
     * @param path 资源路径
     * @param responseListener 加载后的回调
     * @param context 上下文环境
     * @return 资源
     */
    Value loadResource(String path, ResponseListener responseListener, Context context);

}
