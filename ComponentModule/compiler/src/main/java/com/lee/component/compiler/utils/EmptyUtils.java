package com.lee.component.compiler.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019/7/15.
 * @description 字符串、集合判空工具
 */
public class EmptyUtils {

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
