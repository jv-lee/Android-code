package com.jv.code.processkepplive;

import android.util.Log;

/**
 * @author jv.lee
 * @date 2019/10/18.
 * @description
 */
public class KeepTest {

    private static final String TAG = "KeepTest";
    public static int index = 0;

    public static void keepTest() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(5000);
                        Log.i(TAG, "run: 进程保活中" + index++);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}
