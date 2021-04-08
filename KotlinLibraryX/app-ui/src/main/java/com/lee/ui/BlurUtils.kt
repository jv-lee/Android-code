package com.lee.ui

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

/**
 * @author jv.lee
 * @date 2021/3/1
 * @description
 */
object BlurUtils {

    /**
     * @param context 上下文对象
     * @param source bitmap原图对象
     * @param radius 模糊比例 0 - 25
     */
    fun blur(context: Context, source: Bitmap, radius: Float): Bitmap {
        //初始化一个RenderScript Context：通过上下文环境来创建
        val renderScript = RenderScript.create(context)

        //通过renderScript来创建 Allocation
        val input = Allocation.createFromBitmap(renderScript, source)
        val output = Allocation.createTyped(renderScript, input.type)

        //创建ScriptIntrinsic:它内置了RenderScript 的一些通用操作，如高斯模糊、扭曲变换、图像混合
        val scriptIntrinsicBlur =
            ScriptIntrinsicBlur.create(renderScript, Element.RGBA_8888(renderScript))
        //填充数据到Allocation
        scriptIntrinsicBlur.setInput(input)
        //设置模糊半径(0-25)
        scriptIntrinsicBlur.setRadius(radius)
        //启动内核，调用方法处理：调用forEach方法模糊处理
        scriptIntrinsicBlur.forEach(output)
        //从Allocation 中拷贝数据：为了能在Java层访问Allocation的数据
        output.copyTo(source)
        //销毁renderScript对象
        renderScript.destroy()
        return source
    }

}