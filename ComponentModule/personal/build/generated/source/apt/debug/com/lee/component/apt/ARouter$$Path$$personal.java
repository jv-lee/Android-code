package com.lee.component.apt;

import com.lee.component.annotation.model.RouterBean;
import com.lee.component.api.core.ARouterLoadPath;
import com.lee.component.personal.Personal_MainActivity;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

public class ARouter$$Path$$personal implements ARouterLoadPath {
  @Override
  public Map<String, RouterBean> loadPath() {
    Map<String,RouterBean> pathMap = new HashMap<>();
    pathMap.put("/personal/Personal_MainActivity",RouterBean.create(RouterBean.Type.ACTIVITY,Personal_MainActivity.class,"/personal/Personal_MainActivity","personal"));
    return pathMap;
  }
}
