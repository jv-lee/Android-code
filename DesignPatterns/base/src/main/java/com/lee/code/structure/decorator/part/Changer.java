package com.lee.code.structure.decorator.part;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 抽象装饰 变身魔女
 */
public class Changer implements Morrigan {

    Morrigan morrigan;

    public Changer(Morrigan morrigan) {
        this.morrigan = morrigan;
    }

    @Override
    public void display() {
        morrigan.display();
    }
}
