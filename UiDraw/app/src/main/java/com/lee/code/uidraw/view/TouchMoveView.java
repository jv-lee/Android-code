package com.lee.code.uidraw.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author jv.lee
 * @date 2019/5/1
 */
@SuppressLint("AppCompatCustomView")
public class TouchMoveView extends ImageView {

    public TouchMoveView(Context context) {
        super(context);
    }

    public TouchMoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchMoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private int mEndX;
    private int mEndY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(getContext(), "少侠、你可以拖动这个大保健！", Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_MOVE:
                setClickable(false);
                //计算距离上次移动了多远
                int currX = x - mEndX;
                int currY = y - mEndY;
                //设置当前偏移量实现拖动
                this.setTranslationX(this.getTranslationX() + currX);
                this.setTranslationY(this.getTranslationY() + currY);
                break;
            case MotionEvent.ACTION_UP:
                setClickable(true);
                break;
            default:
                break;
        }
        mEndX = x;
        mEndY = y;
        return true;
    }

    /**
     * 可以在　up事件调用 重新回到移动前的远点
     * 平移到初始位置
     */
    private void onReIndex() {
        //平移回到该view水平方向的初始点
        this.setTranslationX(0);
        //判断什么情况下需要回到原点
        if (this.getY() < 0 || this.getY() > (this.getMeasuredHeight() - this.getMeasuredHeight())) {
            this.setTranslationY(0);
        }
    }


}
