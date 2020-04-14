package com.lee.library.adapter.listener;

/**
 * @author jv.lee
 * @date 2020/4/13
 * @description
 */
public interface LoadResource {

    int pageLayoutId();

    int pageLoadingId();

    int pageEmptyId();

    int pageErrorId();

    int pageReloadId();

    int itemLayoutId();

    int itemLoadMoreId();

    int itemLoadEndId();

    int itemLoadErrorId();

    int itemReloadId();
}
