package com.lee.code.structure.proxy;

/**
 * @author jv.lee
 * @date 2020/8/25
 * @description 真实主题1
 */
public class RealSubject1 implements AbstractSubject {
    @Override
    public void request() {
        System.out.println("真实主题1的具体实现");
    }
}
