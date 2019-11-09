package com.lee.code.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Scroller;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jv.lee
 * @date 2019/9/28.
 * @description 大图加载View
 */
public class BigPictureView extends View implements GestureDetector.OnGestureListener, View.OnTouchListener, GestureDetector.OnDoubleTapListener {

    /**
     * 图形绘制区域
     */
    private final Rect mRect;

    /**
     * 内存复用
     */
    private final BitmapFactory.Options mOptions;

    /**
     * 手势识别
     */
    private final GestureDetector mGestureDetector;

    /**
     * 滚动类成员变量
     */
    private final Scroller mScroller;

    /**
     * 缩放手势识别
     */
    private final ScaleGestureDetector mScaleGestureDetector;

    /**
     * 图片的高
     */
    private int mImageHeight;

    /**
     * 图片的宽
     */
    private int mImageWidth;

    /**
     * 图像区域解码器
     */
    private BitmapRegionDecoder mDecoder;

    /**
     * 图像
     */
    private Bitmap mBitmap;

    /**
     * 当前View的宽
     */
    private int mWidth;

    /**
     * 当前View的高
     */
    private int mHeight;

    /**
     * 图像缩放比例
     */
    private float mScale;
    /**
     * 缩放因子
     */
    private float originalScale;

    public BigPictureView(Context context) {
        this(context, null);
    }

    public BigPictureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigPictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //TODO {1.进行初始化操作}
        mRect = new Rect();
        //内存复用
        mOptions = new BitmapFactory.Options();
        //手势识别
        mGestureDetector = new GestureDetector(context, this);
        //滚动类
        mScroller = new Scroller(context);
        //缩放手势识别
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());
        setOnTouchListener(this);
    }

    /**
     * TODO {2.外部设置图片方法 获得图像信息}
     *
     * @param is
     */
    public void setImage(InputStream is) {
        //获取图片宽和高，不能将图片整个加载进内存(设置inJustDecodeBounds 提前获取宽高)
        mOptions.inJustDecodeBounds = true;
        mBitmap = BitmapFactory.decodeStream(is, null, mOptions);
        //提前获取宽高
        mImageWidth = mOptions.outWidth;
        mImageHeight = mOptions.outHeight;

        //开启复用
        mOptions.inMutable = true;
        //设置格式RGB-565 使用更小的内存 比 ARGB-8888 少一半，没有透明通道
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mOptions.inJustDecodeBounds = false;

        //区域解码器
        try {
            mDecoder = BitmapRegionDecoder.newInstance(is, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestLayout();
    }

    /**
     * TODO {3.开始测量,得到View的宽高，测量加载的图片缩放的尺寸}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        //确定加载图片的区域
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = Math.min(mImageWidth, mWidth);
        mRect.bottom = Math.min(mImageHeight, mHeight);

        //计算缩放因子
        originalScale = mWidth / (float) mImageWidth;
        mScale = originalScale;
//        mScale = mWidth / (float) mImageWidth;
//        mRect.bottom = (int) (mHeight / mScale);
    }

    /**
     * TODO {4.绘制具体图像内容}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //判断解码器是否存在，如果未拿到解码器表示没有设置过图像
        if (mDecoder == null) {
            return;
        }
        //设置图像 内存复用, 因为长图在屏幕上显示 始终是一样的大小，只是通过滑动移动，所以Bitmap的宽高一直是一样的，内存区域也是一样的，所以滑动一直是复用同样的内存区域，绘制；
        mOptions.inBitmap = mBitmap;
        //指定解码区域
        mBitmap = mDecoder.decodeRegion(mRect, mOptions);
        Matrix matrix = new Matrix();
        matrix.setScale(mWidth / (float) mRect.width(), mWidth / (float) mRect.width());
        canvas.drawBitmap(mBitmap, matrix, null);
    }

    /**
     * TODO {5.处理Touch事件交付给手势处理器}
     *
     * @param v
     * @param event 事件
     * @return 是否交付处理
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * TODO {6.处理手按下后的处理}
     *
     * @param e 事件
     * @return 是否消费
     */
    @Override
    public boolean onDown(MotionEvent e) {
        //如果移动没有停止，强行停止
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }
        //继续接收后续事件
        return true;
    }

    /**
     * TODO {7.处理滑动事件}
     *
     * @param e1        手指按下开始坐标 （初始坐标）
     * @param e2        获取当前移动的坐标 （实时坐标）
     * @param distanceX (x坐标距离)
     * @param distanceY （y坐标距离）
     * @return 是否消费事件
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //上下移动的时候,mRect需要改变显示的区域
        mRect.offset((int)distanceX, (int) distanceY);
        //移动时，处理到达顶部和底部的情况
        setViewRectSize();
        invalidate();
        return false;
    }

    /**
     * TODO {8.处理惯性}
     *
     * @param e1        手指按下开始坐标 （初始坐标）
     * @param e2        获取当前移动的坐标 （实时坐标）
     * @param velocityX (惯性x轴滑动距离)
     * @param velocityY （惯性y轴滑动距离）
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //使用负数设置惯性滑动距离，国外与我们的滑动理解相反 向上滑动向上移动
        mScroller.fling(mRect.left, mRect.top, (int) -velocityX, (int) -velocityY, 0, (mImageWidth - (int) (mWidth / mScale)), 0, (mImageHeight - (int) (mHeight / mScale)));
        return false;
    }

    /**
     * TODO {9.处理计算结果}
     */
    @Override
    public void computeScroll() {
        if (mScroller.isFinished()) {
            return;
        }
        if (mScroller.computeScrollOffset()) {
            mRect.top = mScroller.getCurrY();
            mRect.bottom = mRect.top + (int) (mHeight / mScale);
            invalidate();
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // 双击事件
        if (mScale < originalScale * 1.5) {
            mScale = originalScale * 3;
        } else {
            mScale = originalScale;
        }
        setViewRectMinSize();
        setViewRectSize();
        invalidate();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    /**
     * 处理缩放回调事件 , 动态设置缩放因子
     */
    class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = mScale;
            //获取上一次事件比例因子
            scale += detector.getScaleFactor() - 1;
            if (scale <= originalScale) {
                scale = originalScale;
            } else if (scale > originalScale * 5) {
                scale = originalScale * 5;
            }
            setViewRectMinSize();
            mScale = scale;
            invalidate();
            return true;
        }
    }

    private void setViewRectSize() {
        if (mRect.bottom > mImageHeight) {
            mRect.bottom = mImageHeight;
            mRect.top = (int) (mImageHeight - (mHeight / mScale));
        }
        if (mRect.top < 0) {
            mRect.top = 0;
            mRect.bottom = (int) (mHeight / mScale);
        }
        if (mRect.right > mWidth) {
            mRect.right = mImageWidth;
            mRect.left = mImageWidth - (int) (mWidth / mScale);
        }
        if (mRect.left < 0) {
            mRect.left = 0;
            mRect.right = (int) (mWidth / mScale);
        }

    }

    private void setViewRectMinSize() {
        mRect.right = mRect.left + (int) (mWidth / mScale);
        mRect.bottom = mRect.top + (int) (mWidth / mScale);
    }

}
