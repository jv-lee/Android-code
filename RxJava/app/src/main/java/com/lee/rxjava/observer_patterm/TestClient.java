package com.lee.rxjava.observer_patterm;

/**
 * @author jv.lee
 * @date 2019-09-12
 * @description
 */
public class TestClient {

    public static void main(String[] args) {
        //观察者们
        Observer observer1 = new ObserverImpl();
        Observer observer2 = new ObserverImpl();
        Observer observer3 = new ObserverImpl();
        Observer observer4 = new ObserverImpl();
        Observer observer5 = new ObserverImpl();

        //被观察者
        Observable observable = new ObservableImpl();

        observable.registerObserver(observer1);
        observable.registerObserver(observer2);
        observable.registerObserver(observer3);
        observable.registerObserver(observer4);
        observable.registerObserver(observer5);

        observable.notifyObservers();

    }
}
