package com.lee.app.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author jv.lee
 * description：注入代理
 */
public class ListenerInvocationHandler implements InvocationHandler {

    /**
     * 我需要拦截MainActivity中的某些方法
     */
    private Object target;

    /**
     * 拦截的简直对
     */
    private HashMap<String, Method> methodMap = new HashMap<>();

    ListenerInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (target != null) {
            String methodName = method.getName();
            method = methodMap.get(methodName);
            if (method != null) {
                return method.invoke(target, args);
            }
        }
        return null;
    }

    /**
     * 拦截的添加
     * @param methodName 本应该执行的方法，onClick()方法 拦截
     * @param method
     */
    void addMethod(String methodName, Method method){
        methodMap.put(methodName, method);
    }
}
