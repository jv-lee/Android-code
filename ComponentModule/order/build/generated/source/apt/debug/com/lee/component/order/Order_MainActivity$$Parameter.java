package com.lee.component.order;

import com.lee.component.api.core.ParameterLoad;
import java.lang.Object;
import java.lang.Override;

public class Order_MainActivity$$Parameter implements ParameterLoad {
  @Override
  public void loadParameter(Object target) {
    Order_MainActivity t = (Order_MainActivity)target;
    t.username = t.getIntent().getStringExtra("username");
  }
}
