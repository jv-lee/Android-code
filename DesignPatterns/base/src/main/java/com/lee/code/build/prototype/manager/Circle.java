package com.lee.code.build.prototype.manager;

import java.util.Scanner;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 原型模式 原型实例 圆
 */
public class Circle implements Shape {
    @Override
    public Object clone() {
        Circle circle = null;
        try {
            circle = (Circle) super.clone();
        } catch (CloneNotSupportedException E) {
            E.printStackTrace();
            System.out.println("拷贝圆失败");
        }
        return circle;
    }

    @Override
    public void countArea(int number) {
        System.out.println("这是一个圆，圆的半径为：" + number);
        System.out.println("该圆的面积为：" + 3.1415 * number * number + "\n");
    }
}
