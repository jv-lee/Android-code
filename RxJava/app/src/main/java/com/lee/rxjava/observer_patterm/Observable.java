package com.lee.rxjava.observer_patterm;

/**
 * @author jv.lee
 * @date 2019-09-12
 * @description 被观察者 标准
 */
public interface Observable {

    /**
     * 在被观察者中 注册观察者
     *
     * @param observer
     */
    void registerObserver(Observer observer);

    /**
     * 在被观察者中 反注册 观察者
     *
     * @param observer
     */
    void unregisterObserver(Observer observer);

    /**
     * 在被观察者中 通知所有被注册的观察者
     */
    void notifyObservers();
}
