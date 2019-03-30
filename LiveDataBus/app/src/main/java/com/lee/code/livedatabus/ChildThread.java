package com.lee.code.livedatabus;

import com.lee.code.livedatabus.livedata.LiveDataBus;

/**
 * @author jv.lee
 * @date 2019/3/30
 */
public class ChildThread extends Thread {

    @Override
    public void run() {
        super.run();
        try {
            Thread.sleep(5000);
            LiveDataBus.getInstance().getChannel("event").postValue("子线程推送消息");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
