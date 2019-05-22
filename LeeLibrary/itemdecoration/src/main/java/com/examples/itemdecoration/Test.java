package com.examples.itemdecoration;

import android.util.Log;

/**
 * @author jv.lee
 * @date 2019/5/22.
 * description：
 */
public class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            System.out.println("groupId:" + i / 5);
            System.out.println("index:" + i % 5);
        }

    }
}
