package com.lee.tabmenu;

public interface ItemTouchHelperAdapter {

    /**
     * item移动
     * @param formPosition
     * @param toPosition
     */
    void onItemMove(int formPosition, int toPosition);

    /**
     * item删除
     * @param position
     */
    void onItemRemove(int position);

}
