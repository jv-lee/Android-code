package com.lee.rxjava.observer_patterm;

/**
 * @author jv.lee
 * @date 2019-09-12
 * @description
 */
public class ObserverImpl implements Observer {

    @Override
    public <T> void changeAction(T observableInfo) {
        System.out.println(observableInfo);
    }

}
