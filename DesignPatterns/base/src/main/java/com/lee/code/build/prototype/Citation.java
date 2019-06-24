package com.lee.code.build.prototype;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 原型模式 生成相似对象
 */
public class Citation implements Cloneable {
    public String name;
    public String info;
    public String college;

    public Citation(String name, String info, String college) {
        this.name = name;
        this.info = info;
        this.college = college;
        System.out.println("奖状创建成功");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void display() {
        System.out.println(name + info + college);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        System.out.println("奖状复制成功");
        return super.clone();
    }
}
