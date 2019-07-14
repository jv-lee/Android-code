package com.lee.component.test;

import com.lee.component.api.core.ARouterLoadGroup;
import com.lee.component.api.core.ARouterLoadPath;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 模拟ARouter路由器组文件
 */
public class ARouter$$Group$$app implements ARouterLoadGroup {

    @Override
    public Map<String, Class<? extends ARouterLoadPath>> loadGroup() {
        HashMap<String, Class<? extends ARouterLoadPath>> groupMap = new HashMap<>(16);
        groupMap.put("app", ARouter$$Path$$app.class);
        return groupMap;
    }
}
