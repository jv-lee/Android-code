package com.lee.library.adapter.listener

/**
 * 设置页面状态资源文件返回接口
 * @author jv.lee
 * @date 2020/4/13
 */
interface LoadResource {
    /**
     * 页面初始状态布局 （加载状态/空页面/错误页面）
     * @return
     */
    fun pageLayoutId(): Int

    /**
     * 加载状态布局id
     * @return
     */
    fun pageLoadingId(): Int

    /**
     * 空页页面布局id
     * @return
     */
    fun pageEmptyId(): Int

    /**
     * 空页面viewId
     */
    fun pageEmptyTextId() :Int

    /**
     * 错误页面布局id
     * @return
     */
    fun pageErrorId(): Int

    /**
     * 无网络布局id
     * @return
     */
    fun pageNetworkId() :Int

    /**
     * 重试加载点击事件id
     * @return
     */
    fun pageReloadId(): Int

    /**
     * 页面加载更多状态布局 （加载更多中/没有更多/加载错误）
     * @return
     */
    fun itemLayoutId(): Int

    /**
     * 加载更多布局id
     * @return
     */
    fun itemLoadMoreId(): Int

    /**
     * 没有更多布局id
     * @return
     */
    fun itemLoadEndId(): Int

    /**
     * 加载错误id
     * @return
     */
    fun itemLoadErrorId(): Int

    /**
     * 错误加载重试事件id
     * @return
     */
    fun itemReloadId(): Int
}