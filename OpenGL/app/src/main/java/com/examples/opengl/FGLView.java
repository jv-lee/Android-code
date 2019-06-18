package com.examples.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author jv.lee
 * @date 2019/6/18.
 * description：
 */
public class FGLView extends GLSurfaceView {
    public FGLView(Context context) {
        super(context);
    }

    public FGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置版本
        setEGLContextClientVersion(2);
        setRenderer(new FGLRender(this));
        //请求渲染
        requestRender();
        /**
         * 设置渲染模式
         * RENDERMODE_CONTINUOUSLY 实时渲染无需调用requestRender（） 每隔16ms渲染一次
         * RENDERMODE_WHEN_DIRTY 按需渲染 (效率高)  手动调用requestRender（）渲染
         */
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    //绘制三角形 等腰三角形 正方形 立方体
}
