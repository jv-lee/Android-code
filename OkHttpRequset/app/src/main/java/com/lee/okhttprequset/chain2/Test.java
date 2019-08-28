package com.lee.okhttprequset.chain2;

/**
 * @author jv.lee
 * @date 2019-08-28
 * @description
 */
public class Test {

    public static void main(String[] args) {
        ChainManager chainManager = new ChainManager();

        chainManager.addTask(new Task1());
        chainManager.addTask(new Task2());
        chainManager.addTask(new Task3());

        chainManager.doRunAction("ok", chainManager);
    }
}
