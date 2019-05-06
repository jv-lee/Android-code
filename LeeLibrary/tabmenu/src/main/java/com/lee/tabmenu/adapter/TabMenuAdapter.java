package com.lee.tabmenu.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.tabmenu.ItemHeadingPosition;
import com.lee.tabmenu.ItemTouchHelperAdapter;
import com.lee.tabmenu.R;
import com.lee.tabmenu.entity.TagEntity;

import java.util.Collections;
import java.util.List;


public class TabMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter,ItemHeadingPosition {
    private List<TagEntity> mTagList;
    public static final int TAB_MENU_ITEM_TYPE_HEADING = 1;
    public static final int TAB_MENU_ITEM_TYPE_SUBHEADING = 2;
    public static final int TAB_MENU_ITEM_TYPE_ITEM = 3;
    public static final int TAB_MENU_ITEM_TYPE_SUB_ITEM = 4;

    public TabMenuAdapter(List<TagEntity> tagList){
        this.mTagList = tagList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TAB_MENU_ITEM_TYPE_HEADING) {
            return new HeadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_heading, parent,false));
        } else if (viewType == TAB_MENU_ITEM_TYPE_SUBHEADING) {
            return new SubHeadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_subheading, parent, false));
        } else if (viewType == TAB_MENU_ITEM_TYPE_ITEM) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false));
        } else if (viewType == TAB_MENU_ITEM_TYPE_SUB_ITEM) {
            return new SubItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TAB_MENU_ITEM_TYPE_HEADING:
                break;
            case TAB_MENU_ITEM_TYPE_SUBHEADING:
                break;
            case TAB_MENU_ITEM_TYPE_ITEM:
                ((ItemViewHolder)viewHolder).bindData(mTagList.get(position));
                break;
            case TAB_MENU_ITEM_TYPE_SUB_ITEM:
                ((SubItemViewHolder)viewHolder).bindData(mTagList.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mTagList == null ? 0:mTagList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mTagList.get(position).getViewType();
    }

    @Override
    public void onItemMove(int formPosition, int toPosition) {
        //设置移动的目标位置必须是我的频道的item位置做交换
        if (getItemViewType(toPosition) == TAB_MENU_ITEM_TYPE_ITEM) {
            Collections.swap(mTagList, formPosition, toPosition);
            notifyItemMoved(formPosition,toPosition);
        }
    }

    @Override
    public void onItemRemove(int position) {
    }

    @Override
    public int onSelectPosition() {
        for (int i = 0; i < mTagList.size(); i++) {
            if (mTagList.get(i).getViewType() == TAB_MENU_ITEM_TYPE_SUBHEADING) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public int onUnSelectPosition() {
        for (int i = 0; i < mTagList.size(); i++) {
            if (mTagList.get(i).getViewType() == TAB_MENU_ITEM_TYPE_SUBHEADING) {
                return i+1;
            }
        }
        return 0;
    }

    public List<TagEntity> getData(){
        return mTagList;
    }

    /**
     * 主标题ViewHolder(我的频道)
     */
    public class HeadingViewHolder extends RecyclerView.ViewHolder{
        HeadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * 副标题ViewHolder（推荐频道）
     */
    public class SubHeadingViewHolder extends RecyclerView.ViewHolder{
        SubHeadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * 选中item（我的频道中的分类）
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView ivClose;
        ImageView ivAdd;
        TextView tvTagName;
        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivClose = itemView.findViewById(R.id.iv_close);
            ivAdd = itemView.findViewById(R.id.iv_add);
            tvTagName = itemView.findViewById(R.id.tv_tag_name);
        }

        void bindData(TagEntity entity) {
            if (!entity.isResident()) {
                ivClose.setVisibility(View.VISIBLE);
                ivAdd.setVisibility(View.GONE);
            }else{
                ivClose.setVisibility(View.GONE);
                ivAdd.setVisibility(View.GONE);
            }
            tvTagName.setText(entity.getName());
            if (!entity.isResident()){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TagEntity remove = mTagList.remove(getAdapterPosition());
                        remove.setClose(false);
                        remove.setAdd(true);
                        remove.setViewType(TAB_MENU_ITEM_TYPE_SUB_ITEM);
                        mTagList.add(onUnSelectPosition(),remove);
                        notifyDataSetChanged();
                    }
                });
            }else{
                itemView.setOnClickListener(null);
            }
        }
    }

    /**
     * 未选中item（推荐频道中的分类）
     */
    public class SubItemViewHolder extends RecyclerView.ViewHolder{
        ImageView ivClose;
        ImageView ivAdd;
        TextView tvTagName;
        SubItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivClose = itemView.findViewById(R.id.iv_close);
            ivAdd = itemView.findViewById(R.id.iv_add);
            tvTagName = itemView.findViewById(R.id.tv_tag_name);
        }
        void bindData(TagEntity entity) {
            ivAdd.setVisibility(View.VISIBLE);
            ivClose.setVisibility(View.GONE);
            tvTagName.setText(entity.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TagEntity remove = mTagList.remove(getAdapterPosition());
                    remove.setClose(true);
                    remove.setAdd(false);
                    remove.setViewType(TAB_MENU_ITEM_TYPE_ITEM);
                    mTagList.add(onSelectPosition(),remove);
                    notifyDataSetChanged();
                }
            });
        }

    }
}
