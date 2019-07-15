package com.lee.component;

import com.lee.component.api.core.ParameterLoad;

/**
 * @author jv.lee
 * @date 2019/7/15.
 * @description
 */
public class XActivity$$Parameter implements ParameterLoad {

    @Override
    public void loadParameter(Object target) {
        //一次
        MainActivity T = (MainActivity) target;

        //循环
        T.name = T.getIntent().getStringExtra("name");
        T.age = T.getIntent().getIntExtra("age", T.age);
    }
}
