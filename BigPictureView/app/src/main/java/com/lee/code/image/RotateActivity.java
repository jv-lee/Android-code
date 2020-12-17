package com.lee.code.image;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lee.code.R;

/**
 * Created by willy on 2017/11/22.
 */

public class RotateActivity extends Activity {

    private Matrix mBaseMatrix = new Matrix();
    private Matrix mSuppMatrix = new Matrix();
    private Matrix mDrawableMatrix = new Matrix();
    private ImageView photoView;
    private float drawableWidth, drawableHeight;
    private float viewWidth, viewHeight;
    private RectF mDisplayRect = new RectF();
    private Drawable bitmap;
    private RectF bitmapRect, imageRect;
    private RotateGestureDetector rotateGestureDetector;
    private RightAngleRunnable mRightAngleRunnable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate);

        photoView = (ImageView) findViewById(R.id.photo_view);
        bitmap = getResources().getDrawable(R.drawable.wallpaper);
        rotateGestureDetector = new RotateGestureDetector();
        rotateGestureDetector.setRotateListener(new IRotateListener() {
            @Override
            public void rotate(int degree, int pivotX, int pivotY) {

                mSuppMatrix.postRotate(degree, pivotX, pivotY);
                //Post the rotation to the image
                checkAndDisplayMatrix();
            }

            @Override
            public void upRotate(int pivotX, int pivotY) {
                float[] v = new float[9];
                mSuppMatrix.getValues(v);
                // calculate the degree of rotation
                int angle = (int) Math.round(Math.toDegrees(Math.atan2(v[Matrix.MSKEW_Y], v[Matrix.MSCALE_X])));
                Toast.makeText(RotateActivity.this, "Angle:"+angle, Toast.LENGTH_SHORT).show();
                Log.v("dyp2", "dypAngle:" + Math.round(Math.toDegrees(Math.atan2(v[Matrix.MSKEW_Y], v[Matrix.MSCALE_X]))));

                mRightAngleRunnable = new RightAngleRunnable(angle, pivotX, pivotY);
                photoView.post(mRightAngleRunnable);
            }
        });


        photoView.post(new Runnable() {
            @Override
            public void run() {
                drawableHeight = bitmap.getIntrinsicHeight();
                drawableWidth = bitmap.getIntrinsicWidth();
                bitmapRect = new RectF(0, 0, drawableHeight, drawableWidth);
                viewHeight = photoView.getHeight();
                viewWidth = photoView.getWidth();
                imageRect = new RectF(0, 0, viewHeight, viewWidth);
                mBaseMatrix.setRectToRect(bitmapRect, imageRect, Matrix.ScaleToFit.CENTER);
                mDrawableMatrix.set(mBaseMatrix);
                photoView.setImageMatrix(mDrawableMatrix);
            }
        });

        photoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rotateGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }


    /**
     * Helper method that simply checks the Matrix, and then displays the result
     */
    private void checkAndDisplayMatrix() {
        if (checkMatrixBounds()) {
            Log.v("dyp4", "photoView.setImageMatrix");
            photoView.setImageMatrix(getDrawMatrix());
        }
    }


    private boolean checkMatrixBounds() {
        RectF rect = getDisplayRect(getDrawMatrix());
        if (rect == null) {
            return false;
        }
        Log.v("dyp", "rect.height:" + rect.height() + ",rect.width:" + rect.width() + "top:" + rect.top + ",bottom:" + rect.bottom + ",left:" + rect.left + ",right:" + rect.right + ",viewHeight:" + viewHeight + ",viewWidth:" + viewWidth);
        final float height = rect.height(), width = rect.width();
        float deltaX = 0, deltaY = 0;

        if (height <= viewHeight) {
            deltaY = (viewHeight - height) / 2 - rect.top;
        } else if (rect.top > 0) {
            deltaY = -rect.top;
        } else if (rect.bottom < viewHeight) {
            deltaY = viewHeight - rect.bottom;
        }


        if (width <= viewWidth) {
            deltaX = (viewWidth - width) / 2 - rect.left;
        } else if (rect.left > 0) {
            deltaX = -rect.left;
        } else if (rect.right < viewWidth) {
            deltaX = viewWidth - rect.right;
        }


        mSuppMatrix.postTranslate(deltaX, deltaY);

        return true;
    }


    private RectF getDisplayRect(Matrix matrix) {
        Drawable d = bitmap;
        if (d != null) {
            mDisplayRect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(mDisplayRect);
            return mDisplayRect;
        }
        return null;
    }

    private Matrix getDrawMatrix() {
        mDrawableMatrix.set(mBaseMatrix);
        mDrawableMatrix.postConcat(mSuppMatrix);
        return mDrawableMatrix;
    }


    class RightAngleRunnable implements Runnable {
        private static final int RECOVER_SPEED = 4;
        private int mOldDegree;
        private int mNeedToRotate;
        private int mRoPivotX;
        private int mRoPivotY;

        RightAngleRunnable(int degree, int pivotX, int pivotY) {
            Log.v("dyp4", "oldDegree:" + degree + "," + "calDegree:" + calDegree(degree));
            this.mOldDegree = degree;
            this.mNeedToRotate = calDegree(degree);
            this.mRoPivotX = pivotX;
            this.mRoPivotY = pivotY;
        }

        /**
         * get right degree,when one finger lifts
         *
         * @param oldDegree current degree
         * @return 0, 90, 180, 270 according to oldDegree
         */
        private int calDegree(int oldDegree) {
            int N = Math.abs(oldDegree) / 45;
            if ((0 <= N && N < 1) || 2 <= N && N < 3) {
                return -oldDegree % 45;
            } else {
                if (oldDegree < 0) {
                    return -(45 + oldDegree % 45);
                } else {
                    return (45 - oldDegree % 45);
                }
            }
        }

        @Override
        public void run() {
            if (mNeedToRotate == 0) {
                return;
            }
            if (photoView == null) {
                return;
            }
            if (mNeedToRotate > 0) {
                //Clockwise rotation
                if (mNeedToRotate >= RECOVER_SPEED) {
                    mSuppMatrix.postRotate(RECOVER_SPEED, mRoPivotX, mRoPivotY);
                    mNeedToRotate -= RECOVER_SPEED;
                } else {
                    mSuppMatrix.postRotate(mNeedToRotate, mRoPivotX, mRoPivotY);
                    mNeedToRotate = 0;
                }
            } else if (mNeedToRotate < 0) {
                //Counterclockwise rotation
                if (mNeedToRotate <= -RECOVER_SPEED) {
                    mSuppMatrix.postRotate(-RECOVER_SPEED, mRoPivotX, mRoPivotY);
                    mNeedToRotate += RECOVER_SPEED;
                } else {
                    mSuppMatrix.postRotate(mNeedToRotate, mRoPivotX, mRoPivotY);
                    mNeedToRotate = 0;
                }
            }


            checkAndDisplayMatrix();
            Compat.postOnAnimation(photoView, this);
        }
    }
}
