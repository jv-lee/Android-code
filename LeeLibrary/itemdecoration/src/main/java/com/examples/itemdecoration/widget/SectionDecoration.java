package com.examples.itemdecoration.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

import com.examples.itemdecoration.entity.GroupInfo;
import com.examples.itemdecoration.utils.SizeUtil;

/**
 * @author jv.lee
 * @date 2019/5/22.
 * description：
 */
public class SectionDecoration extends RecyclerView.ItemDecoration {

    public static final String TAG = "SectionDecoration";

    private GroupInfoCallback mCallback;
    private int mHeaderHeight;
    private int mDividerHeight;

    private TextPaint mTextPaint;
    private Paint mPaint;
    private Paint.FontMetrics mFontMetrics;

    private float mTextOffsetX;

    public SectionDecoration(Context context, GroupInfoCallback callback) {
        mCallback = callback;
        int mTextSize = SizeUtil.sp2px(context, 16);
        mDividerHeight = SizeUtil.dp2px(context, 3);
        mHeaderHeight = SizeUtil.dp2px(context, 30);

        mTextOffsetX = SizeUtil.dp2px(context, 30);

        mHeaderHeight = Math.max(mHeaderHeight, mTextSize);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(mTextSize);
        mFontMetrics = mTextPaint.getFontMetrics();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.YELLOW);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        outRect.top = mHeaderHeight;
        if (mCallback != null) {
            GroupInfo groupInfo = mCallback.getGroupInfo(position);

            //如果是组内的第一个则将间距撑开为一个Header的高度，或者就是普通的分割线高度
            if (groupInfo != null && groupInfo.isFirstViewInGroup()) {
                outRect.top = mHeaderHeight;
            } else {
                outRect.top = mDividerHeight;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(view);

            if (mCallback != null) {
                GroupInfo groupinfo = mCallback.getGroupInfo(index);
                //只有组内的第一个ItemView之上才绘制
                if (groupinfo.isFirstViewInGroup()) {

                    //获取最左边x数值
                    int left = parent.getPaddingLeft();
                    //获取当前itemView最顶部坐标 剪掉 偏移高度，实际是加上 获取到最顶部的高度
                    int top = view.getTop() - mHeaderHeight;
                    //获取最右边x数值
                    int right = parent.getWidth() - parent.getPaddingRight();
                    //获取没有加上偏移量的 itemView的顶部 ，实际就是header高度的底部
                    int bottom = view.getTop();

                    //绘制Header
                    c.drawRect(left, top, right, bottom, mPaint);

                    //在header中的title的向左偏移量
                    float titleX = left + mTextOffsetX;
                    //在header中剧中显示标题
                    float titleY = bottom - ((mHeaderHeight / 2) - mFontMetrics.bottom) + (mFontMetrics.descent / 2);

                    //绘制Title
                    c.drawText(groupinfo.getTitle(), titleX, titleY, mTextPaint);

                }

            }


        }
    }

    public GroupInfoCallback getCallback() {
        return mCallback;
    }

    public void setCallback(GroupInfoCallback callback) {
        this.mCallback = callback;
    }

}
