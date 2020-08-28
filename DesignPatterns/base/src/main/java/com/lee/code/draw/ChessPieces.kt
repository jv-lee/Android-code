package com.lee.code.draw

import android.graphics.Canvas
import android.graphics.Paint

/**
 * @author jv.lee
 * @date 2020/8/28
 * @description 抽象亨元角色：棋子
 */
interface ChessPieces {
    fun downPieces(canvas: Canvas, paint: Paint, point: Point)
}