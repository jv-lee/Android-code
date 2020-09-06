package com.lee.code.behavioral.command.part;

import com.lee.code.core.Tree;

import java.util.ArrayList;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 树枝构件：调用者
 */
public class CompositeInvoker implements AbstractCommand, Tree<AbstractCommand> {

    private ArrayList<AbstractCommand> children = new ArrayList<>();

    @Override
    public void add(AbstractCommand element) {
        children.add(element);
    }

    @Override
    public void remove(AbstractCommand element) {
        children.remove(element);
    }

    @Override
    public AbstractCommand getChild(int index) {
        return children.get(index);
    }

    @Override
    public void execute() {
        for (AbstractCommand child : children) {
            child.execute();
        }
    }
}
