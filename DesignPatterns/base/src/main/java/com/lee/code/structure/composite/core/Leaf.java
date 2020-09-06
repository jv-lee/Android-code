package com.lee.code.structure.composite.core;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 组合模式：树叶构件
 */
public class Leaf implements Component {

    private String name;

    public Leaf(String name) {
        this.name = name;
    }

    @Override
    public void add(Component component) {

    }

    @Override
    public void remove(Component component) {

    }

    @Override
    public Component getChild(int index) {
        return null;
    }

    @Override
    public void operation() {
        System.out.println("树叶" + name + "被访问！");
    }
}
