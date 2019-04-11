package code.lee.code.animation.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author jv.lee
 * @date 2019/4/11
 * 根据路径绘制三角形
 */
public class PathMeasureIconView extends View {

    private Path mPath;
    private Paint mPaint;
    private float mLength;
    private float mAnimValue;
    private PathEffect mEffect;

    private int width;
    private int height;

    private PathMeasure mPathMeasure;

    public PathMeasureIconView(Context context) {
        this(context,null);
    }

    public PathMeasureIconView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathMeasureIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        //初始化数据
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPath = new Path();

        mPath.moveTo((float) (width*0.1), (float) (height*0.1));
        mPath.lineTo((float) (width*0.1), (float) (height * 0.9));
        mPath.lineTo((float) (width * 0.8), (float) (height * 0.5));
        mPath.close();

        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath,true);

        //获取当前绘制的总长度
        mLength =  mPathMeasure.getLength();

        //创建一个属性动画 从0-1
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimValue = (float) animation.getAnimatedValue();
                //设置路径风格 第一个参数为  第二个参数为绘制的偏移量 总长度的百分比
                mEffect = new DashPathEffect(new float[]{mLength,mLength},mLength * mAnimValue);
                mPaint.setPathEffect(mEffect);
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
    }
}
