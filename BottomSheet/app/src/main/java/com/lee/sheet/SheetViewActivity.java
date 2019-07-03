package com.lee.sheet;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SheetViewActivity extends AppCompatActivity {

    BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_view);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //bottomSheet状态改变
                Log.i(">>>", newState + "");
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
            }
        });
    }

    public void switchClick(View view) {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            //显示默认高度
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            //显示最大高度
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        //完全隐藏 behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

}
