package code.lee.code.animation.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * @author jv.lee
 * @date 2019/4/11
 * 根据路径绘制loading图
 */
public class PathMeasureLoadingView extends View {

    private Paint mPaint;
    private Path mDst;
    private Path mPath;
    private float mLength;
    private float mAnimValue;

    private PathMeasure mPathMeasure;


    public PathMeasureLoadingView(Context context) {
        this(context,null);
    }

    public PathMeasureLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathMeasureLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        //初始化数据
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPath = new Path();
        mDst = new Path();

        //通过path设置一个圆形
//        mPath.addCircle(400,400,100,Path.Direction.CW);
        mPath.addCircle(getMeasuredWidth()/2,getMeasuredHeight()/2, (float) (getMeasuredWidth()/2.5),Path.Direction.CW);
        //创建path路径把path设置进去
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath,true);

        //获取当前绘制的总长度
        mLength =  mPathMeasure.getLength();
        //创建一个属性动画 从0-1
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        animator.setInterpolator(new OvershootInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatCount(0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
            mDst.reset();
            //getSegment方法bug处理
            mDst.lineTo(0,0);

            //设置一个起点值0  不断获得绘制的stop值 属性动画的百分比 乘以总长度 慢慢绘制整个圆形

            float stop = mLength * mAnimValue;
            float start = 0;
            //公式表明 开始值在超过百分之50后 开始快速向stop路径靠拢
//        float start = (float) (stop - ((0.5 - Math.abs(mAnimValue - 0.5)) * mLength));

            //设置参数绘制片段
            mPathMeasure.getSegment(start, stop, mDst, true);
            //开始绘制片段
            canvas.drawPath(mDst,mPaint);

    }
}
