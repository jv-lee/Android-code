package com.lee.code.structure.decorator.part;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 抽象装饰 变身魔女-具体形态-女妖
 */
public class Succubus extends Changer {
    public Succubus(Morrigan morrigan) {
        super(morrigan);
    }

    @Override
    public void display() {
        ((Original) super.morrigan).setName("女妖");
        super.display();
    }
}
