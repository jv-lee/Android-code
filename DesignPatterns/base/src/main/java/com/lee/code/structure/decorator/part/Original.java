package com.lee.code.structure.decorator.part;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 实现魔女
 */
public class Original implements Morrigan {
    private String name = "普通魔女";

    @Override
    public void display() {
        System.out.println("我是一个" + name);
    }

    public void setName(String name) {
        this.name = name;
    }
}
