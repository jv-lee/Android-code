package com.lee.code;

import com.lee.code.structure.proxy.AbstractSubject;
import com.lee.code.structure.proxy.DynamicProxy;
import com.lee.code.structure.proxy.RealSubject1;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 动态代理测试案例
 */
public class ProxyTest {

    @Test
    public void test() {
        AbstractSubject subject = null;
        InvocationHandler handler = null;
        handler = new DynamicProxy(new RealSubject1());
        subject = (AbstractSubject) Proxy.newProxyInstance(AbstractSubject.class.getClassLoader(), new Class[]{AbstractSubject.class}, handler);
        subject.request();
    }
}
