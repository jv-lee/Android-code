package com.lee.code.structure.flyweight;

/**
 * @author jv.lee
 * @date 2020/8/28
 * @description 具体亨元角色
 */
public class ConcreteFlyweight implements Flyweight {

    private String key;

    public ConcreteFlyweight(String key) {
        this.key = key;
        System.out.println("具体亨元" + key + "被创建!");
    }

    @Override
    public void operation(UnsharedConcreteFlyweight state) {
        System.out.print("具体亨元" + key + "被调用,");
        System.out.println("非亨元信息是：" + state.getInfo());
    }
}
