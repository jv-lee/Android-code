package code.lee.code.event;

import code.lee.code.event.listener.OnClickListener;
import code.lee.code.event.listener.OnTouchListener;

/**
 * @author jv.lee
 * @date 2019/4/8
 */
public class View {
    private int left;
    private int top;
    private int right;
    private int bottom;

    private OnTouchListener onTouchListener;
    private OnClickListener onClickListener;


    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public View(){

    }

    public View(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean isContainer(int x, int y) {
        if (x >= left && x < right && y >= top && y < bottom) {
            return true;
        }
        return false;
    }

    /**
     * 接受分发的代码
     * @param event
     * @return
     */
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean result = false;

        //设置
        if (onTouchListener != null && onTouchListener.onTouch(this, event)) {
            result = true;
        }

        if (!result && onTouchEvent(event)) {
            result = true;
        }

        return result;
    }


    private boolean onTouchEvent(MotionEvent event) {
        if (onClickListener != null) {
            onClickListener.onClick(this);
            return true;
        }
        return false;
    }


}
