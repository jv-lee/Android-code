package com.lee.component.api.core;

import java.util.Map;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 路由组Group加载数据接口
 */
public interface ARouterLoadGroup {
    /**
     * 加载路由组Group数据
     * 如：“app”，ARouter$$Path$$app.class(实现了ARouterLoadPath接口)
     * @return key:"app",value:"app" 分组对应的路由详细对象类
     */
    Map<String,Class<? extends ARouterLoadPath>> loadGroup();
}
