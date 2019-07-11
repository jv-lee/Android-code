package com.lee.code.build.single;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 创建型模式 - 懒汉单例模式
 * 该模式的特点是类加载时没有生成单列，只有当第一次调用 getInstance 方法时才去创建这个单列
 */
public class LazySingleton {

    /**
     * 保证instance在所有线程中同步
     */
    private static volatile LazySingleton INSTANCE = null;

    private LazySingleton() {
    }

    /**
     * 添加同步锁
     *
     * @return
     */
    public static synchronized LazySingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LazySingleton();
        }
        return INSTANCE;
    }
}
