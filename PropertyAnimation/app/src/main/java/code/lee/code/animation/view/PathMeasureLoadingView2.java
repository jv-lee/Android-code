package code.lee.code.animation.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * @author jv.lee
 * @date 2019/4/11
 * 根据路径绘制loading图
 */
public class PathMeasureLoadingView2 extends View {

    private Paint mPaint;
    private Paint mEfPaint;
    private Path mCircleDst;
    private Path mCirclePath;
    private Path mOkPath;
    private Path mokDst;
    private float mCircleLength;
    private float mCircleAnimValue;
    private float mOkLength;
    private float mOkAnimValue;

    private PathMeasure mPathMeasure;
    private PathMeasure mOkPathMeasure;

    private boolean isOk = false;

    public PathMeasureLoadingView2(Context context) {
        this(context,null);
    }

    public PathMeasureLoadingView2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathMeasureLoadingView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        //初始化数据
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mEfPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEfPaint.setStyle(Paint.Style.STROKE);
        mEfPaint.setStrokeWidth(5);
        mCirclePath = new Path();
        mCircleDst = new Path();
        mOkPath = new Path();
        mokDst = new Path();

        initCircle();
    }

    private void initCircle(){
        //通过path设置一个圆形
        mCirclePath.addCircle(getMeasuredWidth()/2,getMeasuredHeight()/2, (float) (getMeasuredWidth()/2.5),Path.Direction.CW);
        //创建path路径把path设置进去
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mCirclePath,true);

        //获取当前绘制的总长度
        mCircleLength =  mPathMeasure.getLength();
        //创建一个属性动画 从0-1
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatCount(0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCircleAnimValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endFlag = true;
                initOk();
            }
        });
        animator.start();
    }

    private void initOk(){
        //通过path设置一个对勾
        if (isOk) {
            mOkPath.moveTo((float) (getMeasuredWidth()*0.25), (float) (getMeasuredHeight()*0.5));
            mOkPath.lineTo((float) (getMeasuredWidth()*0.45), (float) (getMeasuredHeight()*0.75));
            mOkPath.lineTo((float)( getMeasuredWidth()*0.8),(float) (getMeasuredHeight()*0.4));
        }else{
            mOkPath.moveTo((float) (getMeasuredWidth()*0.5), (float) (getMeasuredHeight()*0.1));
            mOkPath.lineTo((float) (getMeasuredWidth()*0.25), (float) (getMeasuredHeight()*0.75));
            mOkPath.lineTo((float) (getMeasuredWidth()*0.80), (float) (getMeasuredHeight()*0.35));
            mOkPath.lineTo((float) (getMeasuredWidth()*0.2), (float) (getMeasuredHeight()*0.35));
            mOkPath.lineTo((float) (getMeasuredWidth()*0.75), (float) (getMeasuredHeight()*0.75));
            mOkPath.close();
//            mOkPath.moveTo((float)(getMeasuredWidth()*0.25),(float)(getMeasuredHeight() * 0.25));
//            mOkPath.lineTo((float)(getMeasuredWidth() * 0.75), (float) (getMeasuredHeight() * 0.75));
//            mOkPath.lineTo((float) (getMeasuredWidth()*0.75), (float) (getMeasuredHeight()*0.25));
//            mOkPath.lineTo((float) (getMeasuredWidth()*0.25), (float) (getMeasuredHeight()*0.75));
        }


        //创建path路径把path设置测量
        mOkPathMeasure = new PathMeasure();
        mOkPathMeasure.setPath(mOkPath,true);

        //获取当前绘制的总长度
        mOkLength = mOkPathMeasure.getLength();

        //创建一个属性动画 从0-1
        ValueAnimator animator = ValueAnimator.ofFloat(0,1);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatCount(0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOkAnimValue = (float) animation.getAnimatedValue();
                //设置路径风格 第一个参数为  第二个参数为绘制的偏移量 总长度的百分比
//                mEffect = new DashPathEffect(new float[]{mOkLength,mOkLength},mOkLength * mOkAnimValue);
//                mEfPaint.setPathEffect(mEffect);
                postInvalidate();
            }
        });
        animator.start();
    }

    private boolean endFlag = false;
    private boolean end2Flag = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    int  id = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!endFlag) {
            mCircleDst.reset();
            //getSegment方法bug处理
            mCircleDst.lineTo(0,0);

            //设置一个起点值0  不断获得绘制的stop值 属性动画的百分比 乘以总长度 慢慢绘制整个圆形

            float stop = mCircleLength * mCircleAnimValue;
            float start = 0;
            //公式表明 开始值在超过百分之50后 开始快速向stop路径靠拢
//        float start = (float) (stop - ((0.5 - Math.abs(mCircleAnimValue - 0.5)) * mCircleLength));

            //设置参数绘制片段
            mPathMeasure.getSegment(start, stop, mCircleDst, true);
            //开始绘制片段
            canvas.drawPath(mCircleDst,mPaint);

        }
        String current = "end1---";
        if (endFlag) {
            canvas.drawPath(mCirclePath,mPaint);

            mokDst.reset();
            mokDst.lineTo(0,0);

            float stop = mOkLength * mOkAnimValue;
            Log.i(">>>", current + stop);
            float start = 0;
            mOkPathMeasure.getSegment(start, stop, mokDst, true);
            canvas.drawPath(mokDst,mEfPaint);
        }

    }
}
