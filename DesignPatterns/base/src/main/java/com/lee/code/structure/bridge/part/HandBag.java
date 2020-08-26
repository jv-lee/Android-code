package com.lee.code.structure.bridge.part;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 扩展化角色：挎包
 */
public class HandBag extends Bag {

    @Override
    public String getName() {
        return color.getColor() + "HandBag";
    }
}
