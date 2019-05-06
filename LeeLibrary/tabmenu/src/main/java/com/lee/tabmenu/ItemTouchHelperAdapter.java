package com.lee.tabmenu;

public interface ItemTouchHelperAdapter {
    void onItemMove(int formPosition, int toPosition);

    void onItemRemove(int position);

}
