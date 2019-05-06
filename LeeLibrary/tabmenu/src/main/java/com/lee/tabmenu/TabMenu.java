package com.lee.tabmenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

import com.lee.tabmenu.adapter.TabMenuAdapter;
import com.lee.tabmenu.entity.TagEntity;

import java.util.ArrayList;
import java.util.List;

public class TabMenu extends RecyclerView {

    private TabMenuAdapter mTabMenuAdapter;
    private List<TagEntity> mData = new ArrayList<>();

    public TabMenu(@NonNull Context context) {
        this(context,null);
    }

    public TabMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TabMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 绑定view
     */
    public void bindView(){
        mTabMenuAdapter = new TabMenuAdapter(mData);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mTabMenuAdapter.getItemViewType(position) == TabMenuAdapter.TAB_MENU_ITEM_TYPE_HEADING ||
                        mTabMenuAdapter.getItemViewType(position) == TabMenuAdapter.TAB_MENU_ITEM_TYPE_SUBHEADING) {
                    return 4;
                } else{
                    return 1;
                }
            }
        });
        setLayoutManager(gridLayoutManager);
        setAdapter(mTabMenuAdapter);
        ItemTouchCallback itemTouchCallback = new ItemTouchCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(this);
    }

    /**
     * 绑定数据
     * @param list
     */
    public void bindData(List<TagEntity> list) {
        mData.addAll(list);
        mTabMenuAdapter.notifyDataSetChanged();
    }

    /**
     * 获取所有数据集合
     * @return
     */
    public List<TagEntity> getData(){
        if (mData != null) {
            return mData;
        }
        return null;
    }

    /**
     * 获取选中数据集合
     * @return
     */
    public List<TagEntity> getSelectItemData(){
        List<TagEntity> tags = new ArrayList<>();
        if (mData != null) {
            for (TagEntity mDatum : mData) {
                if (mDatum.getViewType() == TabMenuAdapter.TAB_MENU_ITEM_TYPE_ITEM) {
                    tags.add(mDatum);
                }
            }
        }
        return tags;
    }



}
