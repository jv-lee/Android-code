package com.lee.library.adapter.listener;

/**
 * @author jv.lee
 * @date 2020/4/15
 * @description
 */
public interface LoadErrorListener {

    /**
     * 初始页面错误状态重试 （适用于刷新页面失败）
     */
    void pageReload();

    /**
     * 列表数据错误状态重试 ( 适用于分页失败 )
     *
     */
    void itemReload();
}
