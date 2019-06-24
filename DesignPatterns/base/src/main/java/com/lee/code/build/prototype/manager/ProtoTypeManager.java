package com.lee.code.build.prototype.manager;

import java.util.HashMap;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 原型管理器
 */
public class ProtoTypeManager {
    private HashMap<String, Shape> map = new HashMap<>();

    public ProtoTypeManager() {
        map.put("Circle", new Circle());
        map.put("Square", new Square());
    }

    public void addShape(String key, Shape shape) {
        map.put(key, shape);
    }

    public Shape getShape(String key) {
        Shape temp = map.get(key);
        assert temp != null;
        return (Shape) temp.clone();
    }


}
