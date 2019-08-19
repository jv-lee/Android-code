package com.lee.library.utils;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019/7/23.
 * @description 数据对比，找出差异总数
 */
public class CollectionsUtil {

    public static int difference(List list1, List list2, int count) {
        int diffCount = 0;
        if (list1 == null || list2 == null) {
            return 0;
        }
        int size = list1.size() > list2.size() ? list1.size() : list2.size();
        for (int i = 0; i < count; i++) {
            if (i >= size) {
                diffCount++;
                continue;
            }
            if (list1.get(i).equals(list2.get(i))) {
                diffCount++;
            }
        }
        return diffCount;
    }
}
