package com.lee.code.draw

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

/**
 * @author jv.lee
 * @date 2020/8/28
 * @description 具体亨元角色：白子
 */
class WhitePieces : ChessPieces {
    override fun downPieces(canvas: Canvas, paint: Paint, point: Point) {
        paint.color = Color.parseColor("#FFFFFF")
        canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), 10F, paint)
    }

}