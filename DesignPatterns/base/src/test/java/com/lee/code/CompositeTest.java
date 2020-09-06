package com.lee.code;

import com.lee.code.structure.composite.core.Component;
import com.lee.code.structure.composite.core.Composite;
import com.lee.code.structure.composite.core.Leaf;
import com.lee.code.structure.composite.part.Bags;
import com.lee.code.structure.composite.part.Goods;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 组合模式测试案例
 */
public class CompositeTest {

    @Test
    public void test() {
        //案例1
        Composite c1 = new Composite();
        Composite c2 = new Composite();
        Component leaf1 = new Leaf("leaf-1");
        Component leaf2 = new Leaf("leaf-2");
        Component leaf3 = new Leaf("leaf-3");
        c1.add(leaf1);
        c1.add(c2);
        c2.add(leaf2);
        c2.add(leaf3);
        c1.operation();

        //案例2
        float price = 0;
        Bags BigBag, mediumBag, smallRedBag, smallWhiteBag;
        Goods sp;
        BigBag = new Bags("大袋子");
        mediumBag = new Bags("中袋子");
        smallRedBag = new Bags("红色小袋子");
        smallWhiteBag = new Bags("白色小袋子");
        sp = new Goods("婺源特产", 2, 7.9f);
        smallRedBag.add(sp);
        sp = new Goods("婺源地图", 1, 9.9f);
        smallRedBag.add(sp);
        sp = new Goods("韶关香菇", 2, 68);
        smallWhiteBag.add(sp);
        sp = new Goods("韶关红茶", 3, 180);
        smallWhiteBag.add(sp);
        sp = new Goods("景德镇瓷器", 1, 380);
        mediumBag.add(sp);
        mediumBag.add(smallRedBag);
        sp = new Goods("李宁牌运动鞋", 1, 198);
        BigBag.add(sp);
        BigBag.add(smallWhiteBag);
        BigBag.add(mediumBag);
        System.out.println("您选购的商品有：");
        BigBag.show();
        price = BigBag.calculation();
        System.out.println("要支付的总价是：" + price + "$");
    }
}
