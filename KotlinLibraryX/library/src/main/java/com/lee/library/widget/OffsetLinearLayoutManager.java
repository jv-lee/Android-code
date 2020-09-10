package com.lee.library.widget;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

public class OffsetLinearLayoutManager extends LinearLayoutManager {

    public OffsetLinearLayoutManager(Context context) {
        super(context);
    }

    private Map<Integer, Integer> heightMap = new HashMap<>();

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        int count = getChildCount();
        for (int i = 0; i < count ; i++) {
            View view = getChildAt(i);
            heightMap.put(i, view.getHeight());
        }
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        try {
            int firstVisiablePosition = findFirstVisibleItemPosition();
            View firstVisiableView = findViewByPosition(firstVisiablePosition);
            int offsetY = -(int) (firstVisiableView.getY());
            for (int i = 0; i < firstVisiablePosition; i++) {
                offsetY += heightMap.get(i) == null ? 0 : heightMap.get(i);
            }
            return offsetY;
        } catch (Exception e) {
            return 0;
        }
    }
}