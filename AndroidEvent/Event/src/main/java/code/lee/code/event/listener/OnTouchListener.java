package code.lee.code.event.listener;

import code.lee.code.event.MotionEvent;
import code.lee.code.event.View;

/**
 * @author jv.lee
 * @date 2019/4/8
 */
public interface OnTouchListener {
    boolean onTouch(View v, MotionEvent event);
}
