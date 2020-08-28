package com.lee.code.structure.flyweight;

import java.util.HashMap;

/**
 * @author jv.lee
 * @date 2020/8/28
 * @description 亨元工厂
 */
public class FlyweightFactory {
    private HashMap<String, Flyweight> flyweights = new HashMap<>();

    public Flyweight getFlyweight(String key) {
        Flyweight flyweight = flyweights.get(key);
        if (flyweight != null) {
            System.out.println("具体亨元" + key + "已经存在，被获取成功!");
        } else {
            flyweight = new ConcreteFlyweight(key);
            flyweights.put(key, flyweight);
        }
        return flyweight;
    }
}
