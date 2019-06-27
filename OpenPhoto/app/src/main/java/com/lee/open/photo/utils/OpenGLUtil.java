package com.lee.open.photo.utils;

import android.content.Context;
import android.opengl.GLES20;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author jv.lee
 * @date 2019/6/21.
 * description：
 */
public class OpenGLUtil {

    /**
     * 加载raw 文本文件
     *
     * @param context
     * @param rawId
     * @return
     */
    public static String readRawTextFile(Context context, int rawId) {
        InputStream is = context.getResources().openRawResource(rawId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void copyAssets(Context context, String path) {
        File model = new File(path);
        File file = new File(Environment.getExternalStorageDirectory(), model.getName());
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = context.getAssets().open(path);
            int len;
            byte[] b = new byte[2048];
            while ((len = is.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            fos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 加载OpenGL程序
     *
     * @param vSource 顶点着色器代码文件
     * @param fSource 片源着色器代码文件
     * @return OpenGL程序
     */
    public static int loadProgram(String vSource, String fSource) {
        //顶点着色器 加载代码 编译配置
        int vShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vShader, vSource);
        GLES20.glCompileShader(vShader);
        //查看配置是否成功
        int[] status = new int[1];
        GLES20.glGetShaderiv(vShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            //失败
            throw new IllegalStateException("load vertex shader:" + GLES20.glGetShaderInfoLog(vShader));
        }

        //片元着色器 加载代码 编译配置
        int fShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fShader, fSource);
        GLES20.glCompileShader(fShader);
        //查看配置是否成功
        GLES20.glGetShaderiv(fShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            //失败
            throw new IllegalStateException("load fragment shader:" + GLES20.glGetShaderInfoLog(fShader));
        }

        //创建着色器程序
        int program = GLES20.glCreateProgram();
        //绑定顶点和片元
        GLES20.glAttachShader(program, vShader);
        GLES20.glAttachShader(program, fShader);
        //链接着色器
        GLES20.glLinkProgram(program);
        //获得状态
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("link program:" + GLES20.glGetProgramInfoLog(program));
        }
        GLES20.glDeleteShader(vShader);
        GLES20.glDeleteShader(fShader);
        return program;
    }

    public static void glGenTextures(int[] textures) {
        GLES20.glGenTextures(textures.length,textures,0);
        for (int texture : textures) {
            //后面的代码配置纹理，就是bind的这个纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture);
            //配置远端文件显示方式
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);

            //纹理环绕方向 S = X方向   T = Y 方向
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

            //解绑
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }
}
