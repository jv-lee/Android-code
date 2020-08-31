package com.lee.code.structure.composite.core;

import java.util.ArrayList;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 组合模式：树枝构件
 */
public class Composite implements Component {

    private ArrayList<Component> children = new ArrayList<>();

    @Override
    public void add(Component component) {
        children.add(component);
    }

    @Override
    public void remove(Component component) {
        children.remove(component);
    }

    @Override
    public Component getChild(int index) {
        return children.get(index);
    }

    @Override
    public void operation() {
        for (Component child : children) {
            child.operation();
        }
    }
}
