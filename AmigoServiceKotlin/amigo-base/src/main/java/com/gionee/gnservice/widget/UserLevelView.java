package com.gionee.gnservice.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.gionee.gnservice.utils.LogUtil;

import java.util.List;

import static com.gionee.gnservice.utils.ResourceUtil.getColorId;
import static com.gionee.gnservice.utils.ResourceUtil.getDimenId;
import static com.gionee.gnservice.utils.ResourceUtil.getDrawableId;

public class UserLevelView extends View {
    private static final String TAG = UserLevelView.class.getSimpleName();
    private OnLevelClickListener mOnLevelClickListener;
    private List<LevelInfo> mLevelInfoList;
    //等级个数
    private int mLevelCount;
    //当前帐号等级索引
    private int mCurrentAccountIndex = -1;
    //当前选中等级索引
    private int mCurrentDisplayIndex = -1;
    private int mProgressValue = -1;
    private Paint mLinePaint, mLevelTextPaint, mNumberTextPaint;
    //高度
    private int mIconWidth, mIconHeight, mSelectIconHeight;
    //线条颜色，高度
    private int mLineColor;
    private float mLineHeight, mLineWidth;
    //等级和数字文字颜色，选中颜色，字体大小
    private int mLevelTextColor, mNumberTextColor;
    private int mLevelTextSelectColor, mNumberTextSelectColor;
    private float mLevelTextSize, mNumberTextSize;
    private float mMarginTop, mMarginBottom, mLevelTextMarginTop, mNumberTextMarginTop, mSelectDrawableMaiginTop;
    private Paint.FontMetrics mLevelTextFm, mNumberTextFm;
    private float mLevelTextDh, mNumberTextDh;
    //进度圆点
    private Drawable mThumbDrawable;
    //下三角
    private Drawable mSelectDrawable;
    private Rect[] mIconRects;


    public UserLevelView(Context context) {
        this(context, null);
    }

    public UserLevelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initAttributes();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        Resources res = context.getResources();
        mLineColor = res.getColor(getColorId(context, "uc_member_privilege_userlevelview_line_color"));
        mLineWidth = res.getDimension(getDimenId(context, "uc_member_privilege_userlevelview_line_width"));
        mLineHeight = res.getDimension(getDimenId(context, "uc_member_privilege_userlevelview_line_height"));
        mLevelTextColor = res.getColor(getColorId(context, "uc_member_privilege_userlevelview_text_color"));
        mLevelTextSelectColor = res.getColor(getColorId(context, "uc_member_privilege_userlevelview_text_color_select"));
        mLevelTextSize = res.getDimension(getDimenId(context, "uc_member_privilege_userlevelview_text_size"));
        mNumberTextColor = res.getColor(getColorId(context, "uc_member_privilege_userlevelview_number_color"));
        mNumberTextSelectColor = res.getColor(getColorId(context, "uc_member_privilege_userlevelview_number_color_select"));
        mNumberTextSize = res.getDimension(getDimenId(context, "uc_member_privilege_userlevelview_number_text_size"));
        mThumbDrawable = res.getDrawable(getDrawableId(context, "uc_ic_member_privilege_progress_circle"));
        mMarginTop = res.getDimension(getDimenId(context, "uc_member_privilege_userlevelview_margin_top"));
        mMarginBottom = 0;
        mLevelTextMarginTop = res.getDimension(getDimenId(context, "uc_member_privilege_userlevelview_text_margin_top"));
        mNumberTextMarginTop = res.getDimension(getDimenId(context, "uc_member_privilege_userlevelview_number_margin_top"));
        mSelectDrawable = res.getDrawable(getDrawableId(context, "uc_ic_member_privilege_progress_sanjiao"));
        mSelectDrawableMaiginTop = 0;
        mSelectIconHeight = 0;
    }

    private void initAttributes() {
        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setAntiAlias(true);
        mLevelTextPaint = new TextPaint();
        mLevelTextPaint.setTextAlign(Paint.Align.CENTER);
        mLevelTextPaint.setAntiAlias(true);
        mLevelTextPaint.setColor(mLevelTextColor);
        mLevelTextPaint.setTextSize(mLevelTextSize);
        mLevelTextFm = mLevelTextPaint.getFontMetrics();
        mLevelTextDh = mLevelTextFm.bottom - mLevelTextFm.top;
        mNumberTextPaint = new TextPaint();
        mNumberTextPaint.setTextAlign(Paint.Align.CENTER);
        mNumberTextPaint.setAntiAlias(true);
        mNumberTextPaint.setColor(mLevelTextColor);
        mNumberTextPaint.setTextSize(mNumberTextSize);
        mNumberTextFm = mNumberTextPaint.getFontMetrics();
        mNumberTextDh = mNumberTextFm.bottom - mNumberTextFm.top;
    }

    //设置等级信息
    public void setLevelInfoList(List<LevelInfo> levelInfoList) {
        LogUtil.d(TAG);
        if (levelInfoList == null || levelInfoList.size() != 4) {
            throw new IllegalArgumentException("setLevelInfoList params is not vaild");
        }
        mLevelInfoList = levelInfoList;
        mLevelCount = mLevelInfoList.size();
        mIconRects = new Rect[mLevelCount];

        Drawable iconDrawable = getResources().getDrawable(mLevelInfoList.get(0).icon);
        int iconWidth = iconDrawable.getIntrinsicWidth();
        int iconHeight = iconDrawable.getIntrinsicHeight();
        mIconWidth = iconWidth;
        mIconHeight = iconHeight;
        invalidate();

    }

    //设置已选中的等级
    public void setSelectedIndex(int index) {
        if (index > mLevelCount || index < 0) {
            throw new IllegalArgumentException("index value is not vaild");
        }
        if (mCurrentAccountIndex != index) {
            mCurrentAccountIndex = index;
            mCurrentDisplayIndex = index;
            invalidate();
        }

    }

    public int getCurrentAccountIndex() {
        return mCurrentAccountIndex;
    }

    //设置当前进度
    public void setProgress(int value, int maxValue) {
        LogUtil.d(TAG, "set progress value is:" + value + ";maxvalue is:" + maxValue);
        if (value < 0 || maxValue < 0) {
            throw new IllegalArgumentException("set progress params is not vaild");
        }
        if (mProgressValue != value) {
            mProgressValue = value > maxValue ? maxValue : value;
            invalidate();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = 0;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                width = getDesireWidth();
                break;
            default:
                break;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize < getDesireHeight() ? getDesireHeight() : heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = getDesireHeight();
                break;
            case MeasureSpec.UNSPECIFIED:
                height = getDesireHeight();
                break;
            default:
                break;
        }
        LogUtil.d(TAG, "onMeasure width=" + width + ";height=" + height);
        //setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, widthMode), MeasureSpec.makeMeasureSpec(height, heightMode));
        setMeasuredDimension(width, height);
    }


    private int getDesireWidth() {
        return getScreenWidth(getContext());
    }

    public int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    private int getDesireHeight() {
        int height = 0;
        height += mMarginTop;
        height += mIconHeight;
        height += mLevelTextMarginTop;
        height += mLevelTextDh;
        height += mNumberTextMarginTop;
        height += mNumberTextDh;
        height += mSelectDrawableMaiginTop;
        height += mSelectIconHeight;
        height += mMarginBottom;
        return height;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制线条
        drawLines(canvas);
        //绘制进度图标
        //drawThumb(canvas);
        //绘制图标
        drawDrawables(canvas);
        //绘制文字
        drawText(canvas);
        //绘制选中指示图标
        drawSelectIcon(canvas);
    }


    private void drawDrawables(Canvas canvas) {
        int screenWidth = getMeasuredWidth();
        if (mLevelCount <= 0) {
            return;
        }
        int partWidth = screenWidth / mLevelCount;
        Drawable iconDrawable;
        for (int i = 0; i < mLevelCount; i++) {
            if (mCurrentAccountIndex == i) {
                iconDrawable = getResources().getDrawable(mLevelInfoList.get(i).selectIcon);
            } else {
                iconDrawable = getResources().getDrawable(mLevelInfoList.get(i).icon);
            }

            int iconWidth = iconDrawable.getIntrinsicWidth();
            int iconHeight = iconDrawable.getIntrinsicHeight();
            LogUtil.d(TAG, "getIntrinsicWidth is:" + iconWidth);
            int x = partWidth * i + partWidth / 2 - iconWidth / 2;
            int y = (int) mMarginTop;
            initIconRect(x, y, iconWidth, iconHeight, i);
            iconDrawable.setBounds(x, y, x + iconWidth, y + iconHeight);
            iconDrawable.draw(canvas);
        }
    }

//    private void drawLines(Canvas canvas) {
//        int screenWidth = getMeasuredWidth();
//        if (mLevelCount <= 0) {
//            return;
//        }
//        int partWidth = screenWidth / mLevelCount;
//        for (int i = 0; i < mLevelCount; i++) {
//            int iconWidth = mIconWidth;
//            int iconHeight = mIconHeight;
//            float aStartX = i * partWidth;
//            float aStartY = mMarginTop + iconHeight / 2;
//            float aEndX = partWidth * i + partWidth / 2 - iconWidth / 2;
//            float aEndY = aStartY;
//
//            float bStartX = aStartX + partWidth / 2 + iconWidth / 2;
//            float bStartY = aStartY;
//            float bEndX = partWidth * (i + 1);
//            float bEndY = aEndY;
//
//
//            if (i == 0) {
//                canvas.drawLine(bStartX, bStartY, bEndX, bEndY, mLinePaint);
//            } else if (i == mLevelCount - 1) {
//                canvas.drawLine(aStartX, aStartY, aEndX, aEndY, mLinePaint);
//            } else {
//                canvas.drawLine(aStartX, aStartY, aEndX, aEndY, mLinePaint);
//                canvas.drawLine(bStartX, bStartY, bEndX, bEndY, mLinePaint);
//            }
//
//        }
//
//    }

    private void drawLines(Canvas canvas) {
        int screenWidth = getMeasuredWidth();
        if (mLevelCount <= 0) {
            return;
        }
        int partWidth = screenWidth / mLevelCount;
        for (int i = 1; i < mLevelCount; i++) {
            float aStartX = i * partWidth;
            float aStartY = mMarginTop;

            float aEndX = aStartX;
            float aEndY = aStartY + mLineHeight;
            canvas.drawLine(aStartX, aStartY, aEndX, aEndY, mLinePaint);
        }

    }

    private void drawThumb(Canvas canvas) {
        if (mLevelCount <= 0) {
            return;
        }
        if (mProgressValue >= mLevelInfoList.get(mLevelCount - 1).minValue) {
            return;
        }
        int partWidth = getMeasuredWidth() / mLevelCount;
        int lineCount = mLevelCount - 1;
        //1.判断进度图标显示在哪个区域
        int index = 0;
        for (int i = 0; i < lineCount; i++) {
            int minNum = mLevelInfoList.get(i).minValue;
            int maxNum = mLevelInfoList.get(i).maxValue;
            if (i == lineCount - 1) {
                index = i;
                break;
            }
            if (mProgressValue >= minNum && mProgressValue < maxNum) {
                index = i;
                break;
            }

        }
        LogUtil.d(TAG, "thumb location index is:" + index);
        //2.确定进度图标坐标
        LevelInfo levelInfo = mLevelInfoList.get(index);
        int iconWidth = mIconWidth;
        int iconHeight = mIconHeight;

        int x = (mProgressValue - levelInfo.minValue) * (partWidth - iconWidth) / (levelInfo.maxValue - levelInfo.minValue);
        if (x >= partWidth / 2 - iconWidth / 2 && x < partWidth / 2 + iconWidth / 2) {
            x = (partWidth / 2 + iconWidth / 2) + (x - (partWidth / 2 - iconWidth / 2));
        }
        x += x + iconWidth / 2 + partWidth / 2 + index * partWidth;
        int thumbWidth = mThumbDrawable.getIntrinsicWidth();
        int thumbHeight = mThumbDrawable.getIntrinsicHeight();
        mSelectIconHeight = thumbHeight;
        int y = (int) (iconHeight / 2 - thumbHeight / 2 + mMarginTop);
        LogUtil.d(TAG, "thumb location is x=" + x + ";y=" + y + ";width=" + thumbWidth + ";height=" + thumbHeight);
        mThumbDrawable.setBounds(x, y, x + thumbWidth, y + thumbHeight);
        mThumbDrawable.draw(canvas);

    }

    private void drawText(Canvas canvas) {
        int screenWidth = getMeasuredWidth();
        if (mLevelCount <= 0) {
            return;
        }
        int partWidth = screenWidth / mLevelCount;
        Drawable iconDrawable;
        LevelInfo levelInfo;
        for (int i = 0; i < mLevelCount; i++) {
            levelInfo = mLevelInfoList.get(i);
            if (mCurrentAccountIndex == i) {
                iconDrawable = getResources().getDrawable(levelInfo.selectIcon);
            } else {
                iconDrawable = getResources().getDrawable(levelInfo.icon);
            }
            if (mCurrentAccountIndex == i) {
                mLevelTextPaint.setColor(mLevelTextSelectColor);
                mNumberTextPaint.setColor(mNumberTextSelectColor);
            } else {
                mLevelTextPaint.setColor(mLevelTextColor);
                mNumberTextPaint.setColor(mNumberTextColor);
            }
            String name = TextUtils.isEmpty(levelInfo.name) ? "" : levelInfo.name;
            String number = TextUtils.isEmpty(levelInfo.number) ? "" : levelInfo.number;

            float x = i * partWidth + partWidth / 2;
            float y = mMarginTop + iconDrawable.getIntrinsicHeight() + mLevelTextMarginTop + mLevelTextDh / 2.0f - (mLevelTextFm.descent + mLevelTextFm.ascent) / 2;
            canvas.drawText(name, x, y, mLevelTextPaint);

            y = y + mNumberTextMarginTop + mNumberTextDh / 2.0f - (mNumberTextFm.descent + mNumberTextFm.ascent) / 2 + dip2px(getContext(), 3);
            canvas.drawText(number, x, y, mNumberTextPaint);

            float textHeight = mLevelTextDh - mLevelTextFm.descent - mLevelTextFm.ascent;
            float numHeight = mNumberTextDh - mNumberTextFm.descent - mNumberTextFm.ascent;
            expandIconRect(getTextWidth(name, mLevelTextPaint), textHeight, getTextWidth(number, mNumberTextPaint), numHeight, i);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void initIconRect(int x, int y, int iconWidth, int iconHeight, int i) {
        LogUtil.d(TAG, "init icon rect x=" + x + ";y=" + y + ";iconWidth=" + iconWidth + ";iconHeight=" + iconHeight + ";i=" + i);
        mIconRects[i] = new Rect(x, y, x + iconWidth, y + iconHeight);
    }

    private void expandIconRect(float textWidth, float textHeight, float numberWidth, float numberheight, int i) {
        float width = textWidth > numberWidth ? textWidth : numberWidth;
        Rect rect = mIconRects[i];
        if (rect.width() < width) {
            int dx = (int) ((width - rect.width()) / 2);
            rect.left -= dx;
            rect.right += dx;
        }
        rect.bottom += (mLevelTextMarginTop + textHeight + mNumberTextMarginTop + numberheight);
    }

    private float getTextWidth(String text, Paint paint) {
        return paint.measureText(text);
    }

    private void drawSelectIcon(Canvas canvas) {
        if (mSelectDrawable == null || mLevelCount <= 0) {
            return;
        }
        int screenWidth = getMeasuredWidth();
        int partWidth = screenWidth / mLevelCount;
        Drawable drawable = mSelectDrawable;
        int x = partWidth * mCurrentDisplayIndex + partWidth / 2 - drawable.getIntrinsicWidth() / 2;
        int y = getMeasuredHeight() - drawable.getIntrinsicHeight();
        //int y = (int) (mMarginTop + mIconHeight + mLevelTextDh + mNumberTextDh + mLevelTextMarginTop + mNumberTextMarginTop + mSelectDrawableMaiginTop);
        drawable.setBounds(x, y, x + drawable.getIntrinsicWidth(), y + drawable.getIntrinsicHeight());
        drawable.draw(canvas);
    }


    public void setOnLevelClickListener(OnLevelClickListener listener) {
        this.mOnLevelClickListener = listener;
    }

    public interface OnLevelClickListener {
        void onLevelItemClick(int index);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int index = getClickIndex(x, y);
                LogUtil.d(TAG, "onTouchEvent click index is:" + index);
                if (index == -1 || mCurrentDisplayIndex == index) {
                    break;
                }
                if (mOnLevelClickListener != null) {
                    mOnLevelClickListener.onLevelItemClick(index);
                }
                mCurrentDisplayIndex = index;
                invalidate();
                break;
            default:
                break;
        }

        return true;
    }

    private int getClickIndex(float x, float y) {
        for (int i = 0; i < mLevelCount; i++) {
            Rect rect = mIconRects[i];
            LogUtil.d(TAG, "get click index rect is null:" + (rect == null) + ";x=" + x + ";y=" + y);
            if (rect == null) {
                return -1;
            }
            if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
                return i;
            }
        }
        return -1;

    }

    public static class LevelInfo {
        public int icon;
        public int selectIcon;
        public String name;
        public String number;
        public int minValue;
        public int maxValue;
    }
}
