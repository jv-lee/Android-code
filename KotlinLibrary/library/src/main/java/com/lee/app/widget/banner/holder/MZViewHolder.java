package com.lee.app.widget.banner.holder;

import android.content.Context;
import android.view.View;

/**
 *
 * @author Administrator
 * @date 2017/8/15
 */

public interface MZViewHolder<T> {
    /**
     * 创建View
     *
     * @param context
     * @return
     */
    View createView(Context context);

    /**
     * 绑定数据
     *
     * @param context
     * @param position
     * @param data
     */
    void onBind(Context context, int position, T data);
}
