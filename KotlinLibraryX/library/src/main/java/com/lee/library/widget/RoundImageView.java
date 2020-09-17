package com.lee.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lee.library.R;

/**
 * 自定义view - 圆形图片
 *
 * @author jv.lee
 * @date 2019/4/5
 * 圆形图 可设置padding生效 直接获取paddingTop设置圆形图padding值
 * 圆角矩形、椭圆形还未做处理，如需使用 请在onDraw 自行计算修改
 */
@SuppressLint("AppCompatCustomView")
public class RoundImageView extends ImageView {

    /**
     * mWidth:图形宽度
     * mRadius：圆半径
     * mRoundRadius:圆角大小
     * type:记录是圆形还是圆角矩形
     * mRect:矩形凹行大小
     */
    private int mWidth;
    private int mRadius;
    private float mRoundRadius;
    private float mTopLeftRoundRadius;
    private float mTopRightRoundRadius;
    private float mBottomLeftRoundRadius;
    private float mBottomRightRoundRadius;
    private int mType;
    private Paint mPaint;
    private RectF mRect;
    private Matrix mMatrix;

    private boolean scFlag;

    /**
     * 0 圆形、1 圆角矩形、2 椭圆、10 默认圆角大小
     */
    private static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;
    public static final int TYPE_OVAL = 2;
    public static final int DEFAULT_ROUND_RADIUS = 0;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        initView();
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        mType = typedArray.getInt(R.styleable.RoundImageView_layout_mode, TYPE_CIRCLE);
        mRoundRadius = typedArray.getDimension(R.styleable.RoundImageView_radius, DEFAULT_ROUND_RADIUS);
        mTopLeftRoundRadius = typedArray.getDimension(R.styleable.RoundImageView_topLeftRadius, mRoundRadius);
        mTopRightRoundRadius = typedArray.getDimension(R.styleable.RoundImageView_topRightRadius, mRoundRadius);
        mBottomLeftRoundRadius = typedArray.getDimension(R.styleable.RoundImageView_bottomLeftRadius, mRoundRadius);
        mBottomRightRoundRadius = typedArray.getDimension(R.styleable.RoundImageView_bottomRightRadius, mRoundRadius);
        typedArray.recycle();
    }

    /**
     * 属性初始化
     */
    private void initView() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mMatrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //圆形 设置圆角 设置宽高统一
        if (mType == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        try {
            setBitmapShader();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mType == TYPE_CIRCLE) {
            canvas.drawCircle(mRadius, mRadius, mRadius - (getPaddingTop() / 2), mPaint);
        } else if (mType == TYPE_ROUND) {
            mPaint.setColor(Color.RED);
            drawRound(canvas);
        } else if (mType == TYPE_OVAL) {
            canvas.drawOval(mRect, mPaint);
        }
    }

    private void drawRound(Canvas canvas) {
        Path path = new Path();
        path.addRoundRect(mRect, new float[]{
                        mTopLeftRoundRadius, mTopLeftRoundRadius,
                        mTopRightRoundRadius, mTopRightRoundRadius,
                        mBottomLeftRoundRadius, mBottomLeftRoundRadius,
                        mBottomRightRoundRadius, mBottomRightRoundRadius},
                Path.Direction.CW);
        canvas.drawPath(path, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect = new RectF(0, 0, getWidth(), getHeight());
    }

    /**
     * 设置BitmapShader
     */
    private void setBitmapShader() {
        Drawable drawable = getDrawable();
        if (null == drawable) {
            return;
        }
        Bitmap bitmap = drawableToBitmap(drawable);
        // 将bitmap作为着色器来创建一个BitmapShader
        BitmapShader mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (mType == TYPE_CIRCLE) {
            // 拿到bitmap宽或高的小值
            int bSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = mWidth * 1.0f / bSize;

        } else if (mType == TYPE_ROUND || mType == TYPE_OVAL) {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            if (!scFlag) {
                scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
            }
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        mPaint.setShader(mBitmapShader);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable 资源文件
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 设置图片类型：
     *
     * @param mType 圆形、圆角矩形、椭圆形
     */
    public void setType(int mType) {
        if (this.mType != mType) {
            this.mType = mType;
            invalidate();
        }

    }

    /**
     * 设置圆角大小
     *
     * @param mRoundRadius int
     */
    public void setRoundRadius(int mRoundRadius) {
        if (this.mRoundRadius != mRoundRadius) {
            this.mRoundRadius = mRoundRadius;
            this.mTopLeftRoundRadius = mRoundRadius;
            this.mTopRightRoundRadius = mRoundRadius;
            this.mBottomLeftRoundRadius = mRoundRadius;
            this.mBottomRightRoundRadius = mRoundRadius;
            invalidate();
        }
    }

    /**
     * 设置大图是否缩放关闭开关
     *
     * @param flag
     */
    public void scaleCloseEnable(boolean flag) {
        scFlag = flag;
    }

}
