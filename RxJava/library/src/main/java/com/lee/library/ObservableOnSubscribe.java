package com.lee.library;

import android.support.annotation.NonNull;

/**
 * @author jv.lee
 * @date 2019-09-16
 * @description
 */
public interface ObservableOnSubscribe<T> {
    /**
     * Called for each Observer that subscribes.
     *
     * @param emitter the safe emitter instance, never null
     * @throws Exception on error
     */
    void subscribe(@NonNull Observer<T> emitter) throws Exception;
}
