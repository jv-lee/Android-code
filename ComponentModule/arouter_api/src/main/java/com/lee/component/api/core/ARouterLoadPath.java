package com.lee.component.api.core;

import com.lee.component.annotation.model.RouterBean;

import java.util.Map;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 路由组Group对应的详细Path加载数据接口
 * 如：app分组对应有哪些类需要加载
 */
public interface ARouterLoadPath {

    /**
     * 加载路由组Group中的Path详细数据
     * 如：“app”分组下有以下信息：
     *
     * @return key：“/app/MainActivity” value：MainActivity信息封装到RouterBean对象中
     */
    Map<String, RouterBean> loadPath();
}
