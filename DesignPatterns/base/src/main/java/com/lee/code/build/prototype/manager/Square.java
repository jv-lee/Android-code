package com.lee.code.build.prototype.manager;

import java.util.Scanner;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 创建型模式 - 原型模式 原型实例 正方形
 */
public class Square implements Shape {

    @Override
    public Object clone() {
        Square square = null;
        try {
            square = (Square) super.clone();
        } catch (CloneNotSupportedException E) {
            E.printStackTrace();
            System.out.println("正方形拷贝失败");
        }
        return square;
    }

    @Override
    public void countArea(int number) {
        System.out.print("这是一个正方形，它的边长为："+number);
        System.out.println("该正方形的面积=" + number * number + "\n");
    }
}
