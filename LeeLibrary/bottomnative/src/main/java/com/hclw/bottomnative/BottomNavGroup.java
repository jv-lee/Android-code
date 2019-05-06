package com.hclw.bottomnative;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class BottomNavGroup extends RadioGroup implements RadioGroup.OnCheckedChangeListener {

    private String[] textArray;
    private Integer[] btnDrawableArray;
    private Integer textDrawableId;
    private ViewPager mViewPager;
    private ItemPositionListener mItemPositionListener;

    public BottomNavGroup(Context context) {
        this(context,null);
    }

    public BottomNavGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        initChildView();
    }

    private void init(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.BottomNavGroup);
        int buttonArrayId = typedArray.getResourceId(R.styleable.BottomNavGroup_button_array, 0);
        int textArrayId = typedArray.getResourceId(R.styleable.BottomNavGroup_text_array, 0);
        textDrawableId = typedArray.getResourceId(R.styleable.BottomNavGroup_text_background, 0);

        TypedArray buttonArray = getResources().obtainTypedArray(buttonArrayId);
        textArray = getResources().getStringArray(textArrayId);

        btnDrawableArray = new Integer[buttonArray.length()];
        for (int i = 0; i < buttonArray.length(); i++) {
            btnDrawableArray[i] = buttonArray.getResourceId(i, 0);
        }
        buttonArray.recycle();
        typedArray.recycle();
    }

    private void initChildView() {
        RadioButton button = null;
        for (int i = 0; i < btnDrawableArray.length; i++) {
            button = new RadioButton(getContext());
            button.setId(i);
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(null,getResources().getDrawable(btnDrawableArray[i]) ,null,null);
            button.setText(textArray[i]);
            button.setTextColor(getResources().getColorStateList(textDrawableId));

            LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT,1);
            button.setLayoutParams(layoutParams);
            button.setTextSize(12);
            button.setGravity(Gravity.CENTER);
            button.setPadding(0,10,0,0);
            button.setButtonDrawable(null);

            addView(button);
        }
        setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < btnDrawableArray.length; i++) {
            if (i == checkedId) {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(i,false);
                }
                if (mItemPositionListener != null) {
                    mItemPositionListener.onPosition((RadioButton) group.getChildAt(i),i);
                }
                break;
            }
        }
    }

    public interface ItemPositionListener{
        void onPosition(RadioButton button,int position);
    }

    public void setItemPositionListener(ItemPositionListener mItemPositionListener) {
        this.mItemPositionListener = mItemPositionListener;
        check(0);
    }

    public void bindViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }
}
