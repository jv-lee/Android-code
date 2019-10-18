package com.dagger.library;

/**
 * @author jv.lee
 * @date 2019/10/18.
 * @description
 */
public class Preconditions {

    public static <T> T checkNotNull(T value) {
        if (null == value) {
            throw new NullPointerException();
        }
        return value;
    }

    public static <T> T checkNotNull(T value, String errorMessage) {
        if (null == value) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }
}
