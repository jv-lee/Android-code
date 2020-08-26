package com.lee.code.structure.decorator.part;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 抽象装饰 变身魔女-具体形态-少女
 */
public class Girl extends Changer {
    public Girl(Morrigan morrigan) {
        super(morrigan);
    }

    @Override
    public void display() {
        ((Original) super.morrigan).setName("少女");
        super.display();
    }
}
