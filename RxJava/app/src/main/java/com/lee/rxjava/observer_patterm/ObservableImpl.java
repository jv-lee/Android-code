package com.lee.rxjava.observer_patterm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019-09-12
 * @description 被观察者的实现类
 */
public class ObservableImpl implements Observable {

    /**
     * 观察者容器
     */
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.changeAction("被观察者发生了改变");
        }
    }
}
