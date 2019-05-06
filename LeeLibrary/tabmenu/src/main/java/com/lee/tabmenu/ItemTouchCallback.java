package com.lee.tabmenu;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.lee.tabmenu.adapter.TabMenuAdapter;

public class ItemTouchCallback extends ItemTouchHelper.Callback {

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = 0;
        int swipeFlag = 0;

        //特定item才可以拖动
        if (viewHolder instanceof TabMenuAdapter.ItemViewHolder){
            dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        }

        return makeMovementFlags(dragFlag,swipeFlag);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        TabMenuAdapter adapter = (TabMenuAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.onItemMove(viewHolder.getAdapterPosition(),viewHolder1.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }
}
