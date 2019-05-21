package com.lee.library.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019/5/20.
 * description：
 */
public class ProxyAdapter extends RecyclerView.Adapter {
    private List<View> mHeaderViews = new ArrayList<>();
    private List<View> mFooterViews = new ArrayList<>();

    private final RecyclerView.Adapter mAdapter;

    public ProxyAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException();
        }
        mAdapter = adapter;
        setHasStableIds(adapter.hasStableIds());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        final int type = ViewTypeSpec.getType(viewType);
        final int value = ViewTypeSpec.getValue(viewType);

        if (type == ViewTypeSpec.HEADER) {
            viewHolder = new FixedViewHolder(mHeaderViews.get(value));
        } else if (type == ViewTypeSpec.FOOTER) {
            viewHolder = new FixedViewHolder(mFooterViews.get(value));
        } else {
            viewHolder = mAdapter.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FixedViewHolder) {
            ((FixedViewHolder) holder).onBind();
        } else {
            int adjPosition = position - mHeaderViews.size();
            mAdapter.onBindViewHolder(holder, adjPosition);
        }
    }

    @Override
    public int getItemCount() {
        return mHeaderViews.size() + mFooterViews.size() + mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        final int numHeaderView = mHeaderViews.size();

        if (position < numHeaderView) {
            return ViewTypeSpec.makeItemViewTypeSpec(position, ViewTypeSpec.HEADER);
        }

        final int adjPosition = position - numHeaderView;
        final int itemCount = mAdapter.getItemCount();
        if (adjPosition >= itemCount) {
            return ViewTypeSpec.makeItemViewTypeSpec(adjPosition - itemCount, ViewTypeSpec.FOOTER);
        }

        int itemViewType = mAdapter.getItemViewType(adjPosition);
        if (itemViewType < 0 || itemViewType > (1 << ViewTypeSpec.TYPE_SHIFT) - 1) {
            throw new IllegalArgumentException("Invalid item view type: RecyclerView.Adapter.getItemViewType return " + itemViewType);
        }
        return itemViewType;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        mAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        mAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof FixedViewHolder) {
            return;
        }
        mAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof FixedViewHolder) {
            return;
        }
        mAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof FixedViewHolder) {
            return false;
        }
        return mAdapter.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof FixedViewHolder) {
            return;
        }
        mAdapter.onViewRecycled(holder);
    }

    @Override
    public long getItemId(int position) {
        int adjPosition = position - mHeaderViews.size();
        if (adjPosition >= 0 && adjPosition < mAdapter.getItemCount()) {
            return mAdapter.getItemId(adjPosition);
        }

        return RecyclerView.NO_ID;
    }

    private boolean isFixedViewType(int viewType) {
        final int type = ViewTypeSpec.getType(viewType);
        return type == ViewTypeSpec.HEADER || type == ViewTypeSpec.FOOTER;
    }

    public void addHeaderView(View view) {
        if (mHeaderViews.add(view)) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void removeHeaderView(View view) {
        if (mHeaderViews.remove(view)) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addFooterView(View view) {
        if (mFooterViews.add(view)) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void removeFooterView(View view) {
        if (mFooterViews.remove(view)) {
            mAdapter.notifyDataSetChanged();
        }
    }

    static class ViewTypeSpec {
        static final int TYPE_SHIFT = 30;
        static final int TYPE_MASK  = 0x3 << TYPE_SHIFT;

        static final int UNSPECIFIED = 0 << TYPE_SHIFT;
        static final int HEADER = 1 << TYPE_SHIFT;
        static final int FOOTER = 2 << TYPE_SHIFT;

        @IntDef({UNSPECIFIED, HEADER, FOOTER})
        @Retention(RetentionPolicy.SOURCE)
        @interface ViewTypeSpecMode {}

        static int makeItemViewTypeSpec(@IntRange(from = 0, to = (1 << TYPE_SHIFT) - 1) int value,
                                        @ViewTypeSpecMode int type) {
            return (value & ~TYPE_MASK) | (type & TYPE_MASK);
        }

        @ViewTypeSpecMode
        static int getType(int viewType) {
            //noinspection ResourceType
            return (viewType & TYPE_MASK);
        }

        static int getValue(int viewType) {
            return (viewType & ~TYPE_MASK);
        }
    }

    public static class FixedViewHolder extends RecyclerView.ViewHolder {

        FixedViewHolder(View itemView) {
            super(itemView);
            setIsRecyclable(false);
        }

        void onBind() {

        }
    }
}
