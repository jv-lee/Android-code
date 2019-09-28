package com.lee.library;

import android.support.annotation.NonNull;

/**
 * @author jv.lee
 * @date 2019-09-16
 * @description
 */
public interface Function<T,R> {

    @NonNull
    R apply(@NonNull T t) throws Exception;
}
