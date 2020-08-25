package com.lee.code.structure.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author jv.lee
 * @date 2020/8/25
 * @description 代理实例 实现代理功能接口
 */
public class DynamicProxy implements InvocationHandler {
    private Object object;

    public DynamicProxy(Object obj) {
        this.object = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(object, args);
    }
}
