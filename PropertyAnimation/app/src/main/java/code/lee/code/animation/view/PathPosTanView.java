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
public class PathPosTanView extends View implements View.OnClickListener {

    private Path mPath;
    private float[] mPos;
    private float[] mTan;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private ValueAnimator mValueAnimator;
    private float mCurrentValue;

    private int width;
    private int height;


    public PathPosTanView(Context context) {
        this(context,null);
    }

    public PathPosTanView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathPosTanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mPath.addCircle(0,0,200,Path.Direction.CW);

        //设置获取圆形path路径
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath,false);

        mPos = new float[2];
        mTan = new float[2];

        setOnClickListener(this);

        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setDuration(3000);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
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
        //获取path路径的具体位置 按百分比 乘以 当前长度 获取具体pos值 和 tan切线的坐标
        mPathMeasure.getPosTan(mCurrentValue * mPathMeasure.getLength(), mPos, mTan);
        float degree = (float) (Math.atan2(mTan[1],mTan[0]) * 180 /Math.PI);

        canvas.save();
        canvas.translate(400,400);
        canvas.drawPath(mPath,mPaint);
        canvas.drawCircle(mPos[0],mPos[1],10,mPaint);
        canvas.rotate(degree);
        canvas.drawLine(0,-200,300,-200,mPaint);

        canvas.restore();
    }

    @Override
    public void onClick(View v) {
        mValueAnimator.start();
    }
}
