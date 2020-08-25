package com.lee.code.structure.proxy;

/**
 * @author jv.lee
 * @date 2020/8/25
 * @description 真实主题2
 */
public class RealSubject2 implements AbstractSubject {
    @Override
    public void request() {
        System.out.println("真实主题2的具体实现");
    }
}
