package com.lee.component.apt;

import com.lee.component.api.core.ARouterLoadGroup;
import com.lee.component.api.core.ARouterLoadPath;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

public class ARouter$$Group$$personal implements ARouterLoadGroup {
  @Override
  public Map<String, Class<? extends ARouterLoadPath>> loadGroup() {
    Map<String,Class<? extends ARouterLoadPath>> groupMap = new HashMap<>();
    groupMap.put("personal",ARouter$$Path$$personal.class);
    return groupMap;
  }
}
