package com.lee.library.adapter.listener;

/**
 * @author jv.lee
 * @date 2020/4/13
 * @description 设置页面状态资源文件返回接口
 */
public interface LoadResource {

    /**
     * 页面初始状态布局 （加载状态/空页面/错误页面）
     * @return
     */
    int pageLayoutId();

    /**
     * 加载状态布局id
     * @return
     */
    int pageLoadingId();

    /**
     * 空页页面布局id
     * @return
     */
    int pageEmptyId();

    /**
     * 错误页面布局id
     * @return
     */
    int pageErrorId();

    /**
     * 重试加载点击事件id
     * @return
     */
    int pageReloadId();

    /**
     * 页面加载更多状态布局 （加载更多中/没有更多/加载错误）
     * @return
     */
    int itemLayoutId();

    /**
     * 加载更多布局id
     * @return
     */
    int itemLoadMoreId();

    /**
     * 没有更多布局id
     * @return
     */
    int itemLoadEndId();

    /**
     * 加载错误id
     * @return
     */
    int itemLoadErrorId();

    /**
     * 错误加载重试事件id
     * @return
     */
    int itemReloadId();
}
