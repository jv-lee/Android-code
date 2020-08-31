package com.lee.code.behavioral.cor;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 责任链模式：抽象处理角色
 */
public abstract class Handler {
    private Handler next;

    public void setNext(Handler next) {
        this.next = next;
    }

    public Handler getNext() {
        return next;
    }

    /**
     * 处理请求方法
     *
     * @param request 请求参数
     */
    public abstract void handleRequest(String request);
}
