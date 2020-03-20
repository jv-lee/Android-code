package com.lee.component.personal;

import com.lee.component.api.core.ParameterLoad;
import java.lang.Object;
import java.lang.Override;

public class Personal_MainActivity$$Parameter implements ParameterLoad {
  @Override
  public void loadParameter(Object target) {
    Personal_MainActivity t = (Personal_MainActivity)target;
    t.username = t.getIntent().getStringExtra("username");
  }
}
