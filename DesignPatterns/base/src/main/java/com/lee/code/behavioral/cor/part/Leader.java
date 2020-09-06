package com.lee.code.behavioral.cor.part;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 抽象处理对象：领导
 */
public abstract class Leader {
    private Leader next;

    public Leader getNext() {
        return next;
    }

    public void setNext(Leader next) {
        this.next = next;
    }

    public abstract void handleRequest(int leaveDays);
}
