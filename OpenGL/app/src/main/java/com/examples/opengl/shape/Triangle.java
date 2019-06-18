package com.examples.opengl.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author jv.lee
 * @date 2019/6/18.
 * description：三角形
 */
public class Triangle {

    int mProgram;
    /**
     * 渲染等腰三角形
     */
    static float triangleCoords[] = {
            0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };
    float color[] = {1.0f, 1.0f, 1.0f, 1.0f};
    private FloatBuffer vertexBuffer;
    private final String vertextShaderCode = "attribute vec4 vPosition;\n" +
            "void main(){" +
            "gl_Position=vPosition;" +
            "}";
    private final String fragmentShaderCode = "precision mediump float;\n" +
            "uniform vec4 vColor;\n" +
            "void main(){\n" +
            "gl_FragColor=vColor;\t\n" +
            "}";
    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算宽高比
        float ration = (float) width / height;
        //投影矩阵
        Matrix.frustumM(mProjectMatrix, 0, -ration, ration, 0, 1, 3, 120);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7, //摄像机坐标
                0f, 0f, 0f,//目标物的中心点坐标
                0f, 0f, 1f);//相机方向 视角起点

        //计算变换矩阵
        Matrix.multiplyMV(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    public Triangle() {
        //float4个字节 * 4
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        //推送语法给GPU
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
        //创建顶点着色器 并且在GPU进行编译
        int shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(shader, vertextShaderCode);
        GLES20.glCompileShader(shader);

        //创建片元着色器 并且在GPU进行编译
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
        GLES20.glCompileShader(fragmentShader);

        //将片元着色器 和顶点着色器 放到统一程序管理 mProgram
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, shader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        //连接到着色器程序
        GLES20.glLinkProgram(mProgram);
    }

    /**
     * 渲染
     *
     * @param gl
     */
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        int mMatrixHandler = GLES20.glGetAttribLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix2fv(mMatrixHandler, 1, false, mMVPMatrix, 0);

        int mPositionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");

        //允许对gl语言变量读写 打开
        GLES20.glEnableVertexAttribArray(mPositionHandler);

        GLES20.glVertexAttribPointer(mPositionHandler, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        int mColorHandler = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandler, 1, color, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        GLES20.glDisableVertexAttribArray(mPositionHandler);
    }
}
