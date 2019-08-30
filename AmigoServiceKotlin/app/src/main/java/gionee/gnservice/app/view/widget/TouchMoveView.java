package gionee.gnservice.app.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.uniplay.adsdk.utils.ScreenUtil;

/**
 * @author jv.lee
 * @date 2017/5/1
 */
@SuppressLint("AppCompatCustomView")
public class TouchMoveView extends ImageView {

    private int width;
    private int height;
    private int maxWidth;
    private int maxHeight;

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
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        maxWidth = ScreenUtil.getScreenWidth(getContext());
        maxHeight = ScreenUtil.getScreenHeight(getContext()) - getStatusBarHeight();
    }

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
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
                break;
            case MotionEvent.ACTION_MOVE:
                setClickable(false);
                //计算距离上次移动了多远
                int currX = x - mEndX;
                int currY = y - mEndY;
                if (x > (width / 2) && x < (maxWidth - (width / 2)) &&
                        y > (height / 2) && y < (maxHeight - (height / 2))) {
                    moveView(currX, currY);
                }
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

    private void moveView(float x, float y) {
        this.setTranslationX(getTranslationX() + x);
        this.setTranslationY(getTranslationY() + y);
    }

    /**
     * 可以在　up事件调用 重新回到移动前的远点
     * 平移到初始位置
     */
    private void onReIndex() {
        //平移回到该view水平方向的初始点
        if (this.getX() < 0) {
            this.setTranslationX(0);
        }
        //判断什么情况下需要回到原点
        if (this.getY() < 0) {
            this.setTranslationY(0);
        }
    }


}
