package com.lee.rxjava.observer_patterm;

/**
 * @author jv.lee
 * @date 2019-09-12
 * @description 观察者 标准
 */
public interface Observer {

    /**
     * 收到 被观察者 发生改变
     * @param observableInfo
     * @param <T>
     */
    <T> void changeAction(T observableInfo);

}
