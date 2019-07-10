package com.lee.component.apt.test;

import com.lee.component.apt.MainActivity;

/**
 * @author jv.lee
 * @date 2019-07-10
 * @description
 */
public class XActivity$$ARouter {
    public static Class<?> findTargetClass(String path) {
        if ("/app/MainActivity".equalsIgnoreCase(path)) {
            return MainActivity.class;
        }
        return null;
    }
}
