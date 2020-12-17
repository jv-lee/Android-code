package com.lee.code.image;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.lee.code.R;

public class ImageActivity extends AppCompatActivity {

    private Matrix mSuppMatrix = new Matrix();
    private Matrix mBaseMatrix = new Matrix();
    private Matrix mDrawableMatrix = new Matrix();
    private RectF mDisplayRect = new RectF();

    private ScaleGestureDetector scaleGestureDetector;
    private ImageView ivPhoto;
    private float drawableWidth, drawableHeight;
    private float viewWidth, viewHeight;
    private Drawable drawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ivPhoto = (ImageView) findViewById(R.id.photo_view);
        drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
        ivPhoto.setImageDrawable(drawable);

        ivPhoto.post(new Runnable() {
            @Override
            public void run() {
                drawableWidth = drawable.getIntrinsicWidth();
                drawableHeight = drawable.getIntrinsicHeight();

                viewWidth = ivPhoto.getWidth() - ivPhoto.getPaddingLeft() - ivPhoto.getPaddingRight();
                viewHeight = ivPhoto.getHeight() - ivPhoto.getPaddingLeft() - ivPhoto.getPaddingRight();
                Log.v("dyp", "dw:" + drawableWidth + ",dh:" + drawableHeight + ",vw:" + viewWidth + ",vh:" + viewHeight);

                RectF mTempScr = new RectF(0, 0, drawableWidth, drawableHeight);
                RectF mTempDst = new RectF(0, 0, viewWidth, viewHeight);
                mBaseMatrix.setRectToRect(mTempScr, mTempDst, Matrix.ScaleToFit.CENTER);
                mDrawableMatrix.set(mBaseMatrix);
                ivPhoto.setImageMatrix(mDrawableMatrix);
            }
        });


        ivPhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return scaleGestureDetector.onTouchEvent(event);
            }
        });

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                Log.v("dyp","scaleFactor:"+scaleFactor);
                float focusX = detector.getFocusX();
                float focusY = detector.getFocusY();
                if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
                    return false;
                }

                mSuppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
                if(checkMatrixBounds()) {
                    ivPhoto.setImageMatrix(getDrawMatrix());
                }

                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }
        });


    }

    private boolean checkMatrixBounds() {
        RectF rect = getDisplayRect(getDrawMatrix());
        if (rect == null) {
            return false;
        }
        Log.v("dyp","rect.height:"+rect.height()+",rect.width:"+rect.width()+"top:"+rect.top+",bottom:"+rect.bottom+",left:"+rect.left+",right:"+rect.right+",viewHeight:"+viewHeight+",viewWidth:"+viewWidth);
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
            Log.v("dyp","1111");
            deltaX = -rect.left;
        } else if (rect.right < viewWidth) {
            deltaX = viewWidth - rect.right;
            Log.v("dyp","2222");
        }


         mSuppMatrix.postTranslate(deltaX, deltaY);

        return true;
    }


    private RectF getDisplayRect(Matrix matrix) {
        Drawable d = drawable;
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
}
