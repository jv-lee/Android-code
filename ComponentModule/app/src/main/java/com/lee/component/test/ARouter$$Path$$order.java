package com.lee.component.test;

import com.lee.component.annotation.model.RouterBean;
import com.lee.component.api.ARouterLoadPath;
import com.lee.component.order.Order_MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 模拟ARouter路由器组文件，对应的路径
 */
public class ARouter$$Path$$order implements ARouterLoadPath {
    @Override
    public Map<String, RouterBean> loadPath() {
        Map<String, RouterBean> pathMap = new HashMap<>(16);
        pathMap.put("/order/Order_MainActivity",
                RouterBean.create(RouterBean.Type.ACTIVITY,
                        Order_MainActivity.class,
                        "/order/Order_MainActivity",
                        "order"));
        return pathMap;
    }
}
