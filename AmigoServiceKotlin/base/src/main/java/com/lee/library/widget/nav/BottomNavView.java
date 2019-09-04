package com.lee.library.widget.nav;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.lee.library.utils.SizeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019/5/7
 */
public class BottomNavView extends BottomNavigationView implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private List<DotNumberView> dots;
    private ItemPositionListener mItemPositionListener;

    public BottomNavView(Context context) {
        this(context, null);
    }

    public BottomNavView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHorizontalFadingEdgeEnabled(false);
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        for (int i = 0; i < getMenu().size(); i++) {
            if (getMenu().getItem(i) == menuItem) {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(i, false);
                }
                if (mItemPositionListener != null) {
                    mItemPositionListener.onPosition(menuItem, i);
                }
            }
        }
        return true;
    }

    public void toPosition(int position) {
        setSelectedItemId(getMenu().getItem(position).getItemId());
    }

    public void initUnReadMessageViews() {
        //初始化红点view
        BottomNavigationMenuView menuView = null;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof BottomNavigationMenuView) {
                menuView = (BottomNavigationMenuView) child;
                break;
            }
        }
        if (menuView != null) {
            dots = new ArrayList<>();
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                BottomNavigationItemView.LayoutParams params = new BottomNavigationItemView.LayoutParams(SizeUtil.dp2px(getContext(), 15), SizeUtil.dp2px(getContext(), 15));
                params.gravity = Gravity.RIGHT;
                params.topMargin = SizeUtil.dp2px(getContext(), 5);
                params.rightMargin = SizeUtil.dp2px(getContext(), 15);
                DotNumberView dotView = new DotNumberView(getContext());
                itemView.addView(dotView, params);
                dots.add(dotView);
            }
        }
    }

    public void setDotNotRead(final int index, final int number) {
        if (dots != null) {
            dots.get(index).setNumberCount(number);
        }
    }

    public interface ItemPositionListener {
        void onPosition(MenuItem menuItem, int position);
    }

    public void setItemPositionListener(ItemPositionListener mItemPositionListener) {
        this.mItemPositionListener = mItemPositionListener;
    }

    public void bindViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }
}
