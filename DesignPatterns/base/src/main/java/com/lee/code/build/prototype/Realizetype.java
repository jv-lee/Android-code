package com.lee.code.build.prototype;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 原型模式 生成一样的对象
 */
public class Realizetype implements Cloneable {

    public Realizetype() {
        System.out.println("具体原型创建成功");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        System.out.println("具体原型复制成功");
        return super.clone();
    }
}
