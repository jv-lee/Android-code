package com.lee.code.image;

/**
 * Created by willy on 2017/11/22.
 */

public interface IRotateListener {

    /**
     * callback for rotation
     *
     * @param degree degree of rotation
     */
    void rotate(int degree, int pivotX, int pivotY);

    /**
     * MotionEvent.ACTION_POINTER_UP happens when two finger minus to only one
     * change the ImageView to 0,90,180,270
     */
    void upRotate(int pivotX, int pivotY);
}
