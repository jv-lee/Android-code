package com.lee.code.image;

import android.view.MotionEvent;

/**
 * Created by willy on 2017/11/22.
 */

public interface IRotateDetector {

    /**
     * handle rotation in onTouchEvent
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    boolean onTouchEvent(MotionEvent event);

    /**
     * is the Gesture Rotate
     *
     * @return true:rotating;false,otherwise
     */
    boolean isRotating();

}
