package com.lee.component.test;

import com.lee.component.annotation.model.RouterBean;
import com.lee.component.api.core.ARouterLoadPath;
import com.lee.component.personal.Personal_MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 模拟ARouter路由器组文件，对应的路径
 */
public class ARouter$$Path$$personal implements ARouterLoadPath {
    @Override
    public Map<String, RouterBean> loadPath() {
        Map<String, RouterBean> pathMap = new HashMap<>(16);
        pathMap.put("/personal/Personal_MainActivity",
                RouterBean.create(RouterBean.Type.ACTIVITY,
                        Personal_MainActivity.class,
                        "/personal/Personal_MainActivity",
                        "personal"));
        return pathMap;
    }
}
