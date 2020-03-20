package com.lee.component.apt;

import com.lee.component.annotation.model.RouterBean;
import com.lee.component.api.core.ARouterLoadPath;
import com.lee.component.order.Order_MainActivity;
import com.lee.component.order.impl.OrderDrawableImpl;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

public class ARouter$$Path$$order implements ARouterLoadPath {
  @Override
  public Map<String, RouterBean> loadPath() {
    Map<String,RouterBean> pathMap = new HashMap<>();
    pathMap.put("/order/getDrawable",RouterBean.create(RouterBean.Type.CALL,OrderDrawableImpl.class,"/order/getDrawable","order"));
    pathMap.put("/order/Order_MainActivity",RouterBean.create(RouterBean.Type.ACTIVITY,Order_MainActivity.class,"/order/Order_MainActivity","order"));
    return pathMap;
  }
}
