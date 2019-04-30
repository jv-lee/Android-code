package com.lee.tabmenu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.tabmenu.R;
import com.lee.tabmenu.entity.TagEntity;

import java.util.List;


public class TabMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<TagEntity> mTagList;
    public static final int TAB_MENU_ITEM_TYPE_HEADING = 1;
    public static final int TAB_MENU_ITEM_TYPE_SUBHEADING = 2;
    public static final int TAB_MENU_ITEM_TYPE_ITEM = 3;
    public static final int TAB_MENU_ITEM_TYPE_SUB_ITEM = 4;

    public TabMenuAdapter(Context context,List<TagEntity> tagList){
        this.mContext = context;
        this.mTagList = tagList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(">>>", "onCreateViewHolder");
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
        Log.i(">>>", "onBindViewHolder()");
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
//        int size = 0;
//        //获取两个分类列表所有item总数
//        if (mTagsList != null && mTagsList.size() > 0) {
//            for (TagsEntity entity : mTagsList) {
//                size += entity.getTags().size();
//            }
//        }
        return mTagList == null ? 0:mTagList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mTagList.get(position).getViewType();
    }

    class HeadingViewHolder extends RecyclerView.ViewHolder{
        HeadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    class SubHeadingViewHolder extends RecyclerView.ViewHolder{

        SubHeadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
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
            }
            tvTagName.setText(entity.getName());
        }
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder{
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
        }

    }
}
