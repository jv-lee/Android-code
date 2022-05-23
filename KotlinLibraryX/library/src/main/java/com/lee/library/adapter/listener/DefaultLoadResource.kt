package com.lee.library.adapter.listener;

import com.lee.library.R;

/**
 * @author jv.lee
 * @date 2020/4/13
 * @description
 */
public class DefaultLoadResource implements LoadResource {
    @Override
    public int pageLayoutId() {
        return R.layout.layout_page_load;
    }

    @Override
    public int pageLoadingId() {
        return R.id.const_page_loading;
    }

    @Override
    public int pageEmptyId() {
        return R.id.const_page_empty;
    }

    @Override
    public int pageErrorId() {
        return R.id.const_page_error;
    }

    @Override
    public int pageReloadId() {
        return R.id.btn_restart;
    }

    @Override
    public int itemLayoutId() {
        return R.layout.layout_item_load;
    }

    @Override
    public int itemLoadMoreId() {
        return R.id.const_item_loadMore;
    }

    @Override
    public int itemLoadEndId() {
        return R.id.const_item_loadEnd;
    }

    @Override
    public int itemLoadErrorId() {
        return R.id.const_item_loadError;
    }

    @Override
    public int itemReloadId() {
        return R.id.tv_error_text;
    }
}
