package com.lee.component.test;

import com.lee.component.MainActivity;
import com.lee.component.annotation.model.RouterBean;
import com.lee.component.api.core.ARouterLoadPath;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 模拟ARouter路由器组文件，对应的路径
 */
public class ARouter$$Path$$app implements ARouterLoadPath {
    @Override
    public Map<String, RouterBean> loadPath() {
        Map<String, RouterBean> pathMap = new HashMap<>(16);
        pathMap.put("/app/MainActivity",
                RouterBean.create(RouterBean.Type.ACTIVITY,
                        MainActivity.class,
                        "/app/MainActivity",
                        "app"));
        return pathMap;
    }
}
